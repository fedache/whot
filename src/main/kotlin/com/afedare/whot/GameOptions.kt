package com.afedare.whot

data class GameOptions(
    val numOfPlayers: Int = 2,
    val dealNumber: Int = 4,
    val numberOfWhots: Int = 4
) {
    init {
        if (numOfPlayers < 0 || numOfPlayers > 5) throw IllegalArgumentException("Only 1 to 5 players supported")
        if (dealNumber < 1 || dealNumber > 5) throw IllegalArgumentException("Deal card numbers range from 1 to 5")
        if (numberOfWhots < 1 || numberOfWhots > 5) throw IllegalArgumentException("Number of whot cards range 1 to 5")
    }
}