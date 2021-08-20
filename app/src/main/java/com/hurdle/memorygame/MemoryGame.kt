package com.hurdle.memorygame

class MemoryGame(private val boardOption: BoardOption) {
    val cards: List<Card>
    var pairFound = 0
    var numCardFlips = 0
    private var cardSelectionFilter: Int? = null

    init {
        val icons = CARD_ICONS.shuffled().take(boardOption.getNumberPair())
        val shuffledIcons = (icons.shuffled() + icons.shuffled())

        cards = shuffledIcons.map {
            Card(id = it)
        }
    }

    fun flipCard(position: Int): Boolean {
        numCardFlips++
        val card = cards[position]
        var foundCard = false
        // card
        // 0 0이 되면 먼저 뒤집은 카드 재 뒤집기, 선택한 카드도 포함
        // 1 이미지 쌍 매치
        // 2 0이 되면 먼저 뒤집은 카드 재 뒤집기, 선택한 카드도 포함
        if (cardSelectionFilter == null) {
            flipBackCards()
            cardSelectionFilter = position
        } else {
            foundCard = checkMatchCards(cardSelectionFilter!!, position)
            cardSelectionFilter = null
        }
        card.isFlip = !card.isFlip
        return foundCard
    }

    private fun checkMatchCards(cardSelectionFilter: Int, position: Int): Boolean {
        if (cards[cardSelectionFilter].id != cards[position].id) {
            return false
        }
        cards[cardSelectionFilter].isMatch = true
        cards[position].isMatch = true
        pairFound++
        return true
    }

    private fun flipBackCards() {
        cards.forEach { card ->
            if (!card.isMatch) {
                card.isFlip = false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return pairFound == boardOption.getNumberPair()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFlip
    }

    fun getNumMoves(): Int {
        return numCardFlips/2
    }
}