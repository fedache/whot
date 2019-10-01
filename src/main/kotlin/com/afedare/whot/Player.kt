package com.afedare.whot

class Player(val id: Int, cards: List<Card> = emptyList()) {
    private val cards: MutableList<Card> = cards.toMutableList()

    fun pickCard(card: Card) {
        cards.add(card)
    }

    fun playCard(index: Int): Card {
        return cards.removeAt(index)
    }

    fun getCards(): List<Card> = cards
}