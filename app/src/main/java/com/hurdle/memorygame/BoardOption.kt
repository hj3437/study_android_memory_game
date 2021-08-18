package com.hurdle.memorygame

const val CARD_NUMBER_MIN = 8
const val CARD_NUMBER_MIDDLE = 18
const val CARD_NUMBER_MAX = 24

enum class BoardOption(val cardNumber: Int) {
    BOARD_MIN(CARD_NUMBER_MIN),
    BOARD_MEDIUM(CARD_NUMBER_MIDDLE),
    BOARD_MAX(CARD_NUMBER_MAX);

    fun getWidth(): Int {
        return when (this) {
            BOARD_MIN -> 2
            BOARD_MEDIUM -> 3
            BOARD_MAX -> 4
        }
    }

    fun getHeight(): Int = cardNumber / getWidth()

    fun getNumberPair(): Int = cardNumber / 2
}