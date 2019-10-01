package com.afedare.whot

import java.lang.IllegalArgumentException

data class Card(val shape: Shape, val number: Int) {
    init {
        if (!isValidCard(shape, number))
            throw IllegalArgumentException("Invalid card combination of shape and number")
    }

    fun holdOn(): Boolean = number == HOLD_ON_NUMBER
    fun generalMarket(): Boolean = number == GENERAL_MARKET_NUMBER
    fun pickTwo(): Boolean = number == PICK_TWO_NUMBER
    fun pickThree(): Boolean = number == PICK_THREE_NUMBER
    fun suspension(): Boolean = number == SUSPENSION
    fun whot(): Boolean = shape == Shape.WHOT


    companion object {
        const val HOLD_ON_NUMBER = 1
        const val GENERAL_MARKET_NUMBER = 14
        const val PICK_TWO_NUMBER = 2
        const val PICK_THREE_NUMBER = 5
        const val SUSPENSION = 8
        const val WHOT_NUMBER = 20

        fun isValidCard(shape: Shape, num: Int): Boolean {
            if (num == 6 || num == 9) return false
            if ((shape == Shape.CROSSES || shape == Shape.SQUARES) && (num == 4 || num == 8 || num == 12)) return false
            if (shape == Shape.STARS && num > 8) return false
            if (shape == Shape.WHOT && num != WHOT_NUMBER) return false
            return true
        }
    }
}