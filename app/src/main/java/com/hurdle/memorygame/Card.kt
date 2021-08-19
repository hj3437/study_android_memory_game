package com.hurdle.memorygame

data class Card(
    val id:Int,
    var isFlip: Boolean = true,
    var isMatch: Boolean = false
)
