package com.afedare.whot

object Cards {
    private const val maxCardNum = 14
    private const val minCardNum = 1

    fun allCards(numberOfWhot: Int = 5): List<Card> {
        val allCards = mutableListOf<Card>()
        for (shape in Shape.SHAPES_WITHOUT_WHOT) {
            for (num in minCardNum..maxCardNum) {
                if (Card.isValidCard(shape, num)) {
                    allCards.add(Card(shape, num))
                }
            }
        }

        // Add whot cards
        for (i in 1..numberOfWhot)
            allCards.add(Card(Shape.WHOT, Card.WHOT_NUMBER))
        return allCards
    }
}