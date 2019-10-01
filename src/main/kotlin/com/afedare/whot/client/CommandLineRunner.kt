package com.afedare.whot.client

import com.afedare.whot.*
import java.util.*

object CommandLineRunner {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Welcome to whot game")
        val scanner = Scanner(System.`in`)
        print("Number of players: ")
        val numberOfPlayers = scanner.nextInt()

        val players = mutableSetOf<Player>()
        for (i in 1..numberOfPlayers) {
            players.add(Player(i))
        }
        val game = Game(players, GameOptions(numberOfPlayers))
        do {
            val currentPlayer = game.currentPlayer()
            val floorCard = game.floorCard()
            if (floorCard != null) {
                println("Floor card: ${floorCard.cardStr()}")
            }
            println("Player ${currentPlayer.id}")
            showCards(currentPlayer)

            print("Play Card (card number) or Go to market (0): ")
            val cardNumber = scanner.nextInt()
            if (cardNumber == 0) {
                game.currentPlayerGotoGen()
            } else {
                val userCanAskForCard = game.playCard(cardNumber - 1)
                if (game.currentPlayerWon()) {
                    println("Player ${currentPlayer.id} won")
                    break
                } else if (userCanAskForCard) {
                    print("Ask next user for a shape ${Shape.SHAPES_WITHOUT_WHOT.joinToString { it.symbol }} (1-${Shape.SHAPES_WITHOUT_WHOT.size}):")
                    val shapeIndex = scanner.nextInt()
                    game.askForCardShape(Shape.SHAPES_WITHOUT_WHOT[shapeIndex - 1])
                }
            }
            game.advancePlay()
            println()
        } while (true)
    }

    private fun showCards(player: Player) {
        val cards = player.getCards()
        var index = 0
        println(cards.joinToString { card -> index++; "$index.${card.cardStr()}" })
    }

    private fun Card.cardStr() = "[$number ${shape.symbol}]"
}