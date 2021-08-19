package com.hurdle.memorygame

class MemoryGame(private val boardOption: BoardOption) {
    val cards: List<Card>
    val pairFound = 0

    init {
        val icons = CARD_ICONS.shuffled().take(boardOption.getNumberPair())
        val shuffledIcons = (icons.shuffled() + icons.shuffled())

        cards = shuffledIcons.map {
            Card(id = it)
        }
    }

    fun flipCard(position: Int) {
        val card = cards[position]
        card.isFlip = !card.isFlip
    }
}