package com.afedare.whot

import java.lang.IllegalArgumentException
import java.util.*

class Game(
    private val players: Set<Player>,
    private val options: GameOptions = GameOptions(),
    shuffledCards: List<Card> = Cards.allCards(options.numberOfWhots).shuffled(),
    private val playStack: Stack<Card> = Stack(),
    private var pendingGameFlowActions: GameFlowActions = GameFlowActions.NORMAL
) {

    private val shuffledCardsStack: MutableList<Card>
    private var currentPlayerIndex = 0
    private var currentRequestShape: Shape? = null
    private val _currentPlayer: Player get() = players.elementAt(currentPlayerIndex)

    init {
        if (players.size < 2) throw IllegalArgumentException("Game involves 2 players")
        shuffledCardsStack = shuffledCards.toMutableList()
        if (players.all { player -> player.getCards().isEmpty() }) {
            dealCards()
        }
    }

    private fun dealCards() {
        for (i in 1..options.dealNumber) {
            for (player in players) {
                val card = shuffledCardsStack.removeLastCard()
                player.pickCard(card)
            }
        }
    }

    fun currentPlayerGotoGen() {
        pendingGameFlowActions = GameFlowActions.CURRENT_TO_MARKET
    }

    fun currentPlayer(): Player = _currentPlayer

    /**
     * User plays card at given index
     * @return if user gets to ask for a new card (WHOT)
     */
    fun playCard(index: Int): Boolean {
        val currentPlayer = _currentPlayer
        val selectedCard = currentPlayer.getCards().elementAt(index)
        pendingGameFlowActions = if (isValidPlay(selectedCard)) {
            currentPlayer.playCard(index)
            playStack.push(selectedCard)
            when {
                selectedCard.holdOn() -> GameFlowActions.HOLD_ON
                selectedCard.generalMarket() -> GameFlowActions.GENERAL_MARKET
                selectedCard.pickTwo() -> GameFlowActions.PICK_TWO
                selectedCard.pickThree() -> GameFlowActions.PICK_THREE
                selectedCard.suspension() -> GameFlowActions.SUSPENSION
                else -> GameFlowActions.NORMAL
            }
        } else {
            // let the player play again
            GameFlowActions.SUSPENSION
        }

        if (selectedCard.whot()) {
            return true
        }
        return false
    }

    // TODO: Make public for clients, Has to do more with messages
    private fun isValidPlay(card: Card): Boolean {
        if (playStack.isEmpty()) return true
        val floorCard = playStack.peek()
        return if (floorCard.whot()) {
            val shape = currentRequestShape
            if (shape != null) {
                card.shape == shape || card.whot()
            } else {
                throw IllegalArgumentException("Invalid state")
            }
        } else {
            floorCard.shape == card.shape || floorCard.number == card.number || card.whot()
        }
    }

    fun currentPlayerWon(): Boolean {
        return _currentPlayer.getCards().isEmpty()
    }

    fun askForCardShape(shape: Shape) {
        currentRequestShape = shape
    }

    fun floorCard(): Card? = if (playStack.isEmpty()) null else playStack.peek()

    fun advancePlay(): AdvancePlayError? {
        if (pendingGameFlowActions == GameFlowActions.HOLD_ON) {
            // don't advance play
        } else if (pendingGameFlowActions == GameFlowActions.SUSPENSION) {
            changeNextPlayer(2)
        } else if (pendingGameFlowActions == GameFlowActions.PICK_TWO) {
            changeNextPlayer(1)
            if (!playerPickCard(2)) return AdvancePlayError.OUT_OF_CARD
            changeNextPlayer(1)
        } else if (pendingGameFlowActions == GameFlowActions.PICK_THREE) {
            changeNextPlayer(1)
            if (!playerPickCard(3)) return AdvancePlayError.OUT_OF_CARD
            changeNextPlayer(1)
        } else if (pendingGameFlowActions == GameFlowActions.GENERAL_MARKET) {
            // everyone picks a card
            for (player in players) {
                if (_currentPlayer == player) continue
                if (!playerPickCard(1, player)) return AdvancePlayError.OUT_OF_CARD
            }
            // don't advance play
        } else if (pendingGameFlowActions == GameFlowActions.CURRENT_TO_MARKET) {
            if (!playerPickCard()) return AdvancePlayError.OUT_OF_CARD
            changeNextPlayer(1)
        } else if (pendingGameFlowActions == GameFlowActions.NORMAL) {
            changeNextPlayer(1)
        }
        pendingGameFlowActions = GameFlowActions.NORMAL
        return null
    }

    private fun playerPickCard(numCard: Int = 1, player: Player = _currentPlayer): Boolean {
        for (i in 0 until numCard) {
            val card = fromMarket() ?: return false
            player.pickCard(card)
        }
        return true
    }

    private fun fromMarket(): Card? {
        if (shuffledCardsStack.isEmpty()) {
            // keep top most element in play stack
            val floorCard = playStack.pop()
            if (playStack.isEmpty())
                return null
            playStack.shuffle()
            while (playStack.isNotEmpty()) {
                shuffledCardsStack.add(playStack.pop())
            }
            playStack.push(floorCard)
        }
        return shuffledCardsStack.removeLastCard()
    }

    private fun changeNextPlayer(byNum: Int) {
        currentPlayerIndex = (currentPlayerIndex + byNum) % players.size
    }
}

private fun MutableList<Card>.removeLastCard() = this.removeAt(this.size - 1)

enum class GameFlowActions {
    HOLD_ON,
    SUSPENSION,
    PICK_TWO,
    PICK_THREE,
    GENERAL_MARKET,
    CURRENT_TO_MARKET,
    NORMAL,
}

enum class AdvancePlayError {
    OUT_OF_CARD,
    WRONG_CARD
}