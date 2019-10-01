package com.afedare.whot

import com.afedare.whot.kotlin.stackOf
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import java.util.*
import com.afedare.whot.Shape.*

class GameTest {
//    val shuffleCards = listOf(Card(shape= STARS, number=8), Card(shape=WHOT, number=20), Card(shape=SQUARES, number=1), Card(shape=CIRCLE, number=5), Card(shape=TRIANGLES, number=13), Card(shape=CIRCLE, number=7), Card(shape=WHOT, number=20), Card(shape=CROSSES, number=3), Card(shape=CROSSES, number=11), Card(shape=STARS, number=4), Card(shape=CROSSES, number=13), Card(shape=TRIANGLES, number=11), Card(shape=WHOT, number=20), Card(shape=CROSSES, number=14), Card(shape=WHOT, number=20), Card(shape=STARS, number=5), Card(shape=CIRCLE, number=13), Card(shape=CROSSES, number=2), Card(shape=CIRCLE, number=11), Card(shape=SQUARES, number=5), Card(shape=TRIANGLES, number=7), Card(shape=STARS, number=2), Card(shape=CIRCLE, number=14), Card(shape=TRIANGLES, number=5), Card(shape=CIRCLE, number=8), Card(shape=CIRCLE, number=3), Card(shape=SQUARES, number=13), Card(shape=TRIANGLES, number=4), Card(shape=CIRCLE, number=2), Card(shape=TRIANGLES, number=2), Card(shape=SQUARES, number=7), Card(shape=STARS, number=3), Card(shape=TRIANGLES, number=14), Card(shape=TRIANGLES, number=8), Card(shape=CROSSES, number=5), Card(shape=TRIANGLES, number=1), Card(shape=SQUARES, number=2), Card(shape=CIRCLE, number=10), Card(shape=CIRCLE, number=12), Card(shape=CROSSES, number=10), Card(shape=SQUARES, number=14), Card(shape=TRIANGLES, number=3), Card(shape=CIRCLE, number=1), Card(shape=SQUARES, number=11), Card(shape=SQUARES, number=3), Card(shape=CROSSES, number=1), Card(shape=SQUARES, number=10), Card(shape=STARS, number=7), Card(shape=STARS, number=1), Card(shape=CIRCLE, number=4), Card(shape=TRIANGLES, number=12), Card(shape=CROSSES, number=7), Card(shape=TRIANGLES, number=10))

    @Test
    fun `test pick 3 cards`() {
        val player1 = Player(
            1, listOf(
                Card(CIRCLE, 5),
                Card(TRIANGLES, 12)
            )
        )
        val player2 =
            Player(
                2, listOf(
                    Card(TRIANGLES, 7),
                    Card(STARS, 2)
                )
            )

        val shuffleCards = listOf(
            Card(SQUARES, 13),
            Card(TRIANGLES, 4),
            Card(CIRCLE, 2)
        )
        val game = Game(
            setOf(player1, player2),
            GameOptions(),
            shuffleCards,
            stackOf(),
            GameFlowActions.NORMAL
        )

        game.playCard(0)
        game.advancePlay()
        assertEquals("Player card reduced", player2.getCards().size, 5)
    }

    @Test
    fun `test pick 2 cards`() {
        val player1 = Player(
            1, listOf(
                Card(CIRCLE, 5),
                Card(TRIANGLES, 12)
            )
        )
        val player2 =
            Player(
                2, listOf(
                    Card(TRIANGLES, 7),
                    Card(STARS, 2)
                )
            )

        val shuffleCards = listOf(
            Card(SQUARES, 13),
            Card(TRIANGLES, 4),
            Card(CIRCLE, 2)
        )
        val game = Game(
            setOf(player2, player1),
            GameOptions(),
            shuffleCards,
            stackOf(),
            GameFlowActions.NORMAL
        )

        game.playCard(1)
        game.advancePlay()
        assertEquals("Player card reduced", player1.getCards().size, 4)
    }

    @Test
    fun `test all users hold on`() {
        val player1 = Player(
            1, listOf(
                Card(TRIANGLES, 1),
                Card(TRIANGLES, 12)
            )
        )
        val player2 = Player(
            2, listOf(
                Card(TRIANGLES, 7),
                Card(STARS, 2)
            )
        )

        val shuffleCards = listOf(
            Card(SQUARES, 13),
            Card(TRIANGLES, 4),
            Card(CIRCLE, 2)
        )

        val game = Game(
            setOf(player1, player2),
            GameOptions(),
            shuffleCards,
            stackOf(Card(TRIANGLES, 10)),
            GameFlowActions.NORMAL
        )

        game.playCard(0)
        game.advancePlay()

        game.playCard(0)
        assertEquals("Player card reduced", player1.getCards().size, 0)
        assertEquals("Player 1 won", game.currentPlayerWon(), true)
        game.advancePlay()
    }

    @Test
    fun `test suspension users hold on`() {
        val player1 = Player(
            1, listOf(
                Card(TRIANGLES, 8),
                Card(TRIANGLES, 12)
            )
        )
        val player2 = Player(
            2, listOf(
                Card(TRIANGLES, 7),
                Card(STARS, 2)
            )
        )

        val player3 = Player(
            2, listOf(
                Card(STARS, 7),
                Card(STARS, 3)
            )
        )

        val shuffleCards = listOf(Card(SQUARES, 13))

        val game = Game(
            setOf(player1, player2, player3),
            GameOptions(),
            shuffleCards,
            stackOf(Card(TRIANGLES, 10)),
            GameFlowActions.NORMAL
        )

        game.playCard(0)
        game.advancePlay()

        assertEquals("Player 3 is next", game.currentPlayer().id, 3)
    }
}