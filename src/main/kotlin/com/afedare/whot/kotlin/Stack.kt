package com.afedare.whot.kotlin

import java.util.*

fun <T> stackOf(vararg elements: T): Stack<T> {
    val stack = Stack<T>()
    for (elem in elements) {
        stack.push(elem)
    }
    return stack
}