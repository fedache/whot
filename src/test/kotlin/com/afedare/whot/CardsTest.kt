package com.afedare.whot

import junit.framework.Assert.assertEquals
import org.junit.Test

class CardsTest {
    @Test
    fun testGetAllCards() {
        assertEquals("All cards should be 54", 54, Cards.allCards().size)
        assertEquals("All cards should be 53", 53, Cards.allCards(4).size)
    }
}