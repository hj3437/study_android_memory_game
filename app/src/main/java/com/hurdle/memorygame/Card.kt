package com.hurdle.memorygame

data class Card(
    val id:Int,
    var isFlip: Boolean = false,
    var isMatch: Boolean = false
)
