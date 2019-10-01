package com.afedare.whot

enum class Shape(val symbol: String) {
    CIRCLE("●"),
    TRIANGLES("▲"),
    CROSSES("✚"),
    SQUARES("◼"),
    STARS("★"),
    WHOT("W");

    companion object {
        val SHAPES_WITHOUT_WHOT = listOf(CIRCLE, TRIANGLES, CROSSES, SQUARES, STARS)
    }
}

