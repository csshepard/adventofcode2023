data class Hand(
    val cards: List<Char>,
    val jacksWild: Boolean = false
): Comparable<Hand> {

    private val handType: HandType get() {
        val cardSet = mutableMapOf<Char, Int>()
        cards.forEach { card -> cardSet[card] = cardSet.getOrDefault(card, 0) + 1 }
        if (jacksWild && 'J' in cardSet) {
            val maxCard = cardSet.filter{it.key != 'J'}.maxByOrNull { (_, count) -> count}?.key
            if (maxCard != null) {
                cardSet[maxCard] = cardSet[maxCard]!! + cardSet['J']!!
                cardSet.remove('J')
            }
        }
        return when (cardSet.size) {
            1 -> HandType.FIVE_OF_A_KIND
            2 -> {
                if (cardSet.any { entry -> entry.value == 4 })
                    HandType.FOUR_OF_A_KIND
                else HandType.FULL_HOUSE
            }
            3 -> {
                if (cardSet.any { entry -> entry.value == 3 })
                    HandType.THREE_OF_A_KIND
                else HandType.TWO_PAIR
            }
            4 -> HandType.ONE_PAIR
            else -> HandType.HIGH_CARD
        }
    }

    override fun compareTo(other: Hand): Int {
        if (this.handType != other.handType) {
            return this.handType.rank.compareTo(other.handType.rank)
        }
        return this.cards.zip(other.cards).firstOrNull { (card1, card2) -> card1 != card2 }
            ?.let { (card1, card2) -> card1.toStrength(jacksWild).compareTo(card2.toStrength(jacksWild)) } ?: 0
    }
}

fun Char.toStrength(jacksWild: Boolean = false): Int {
    return when (this) {
        '2' -> 2
        '3' -> 3
        '4' -> 4
        '5' -> 5
        '6' -> 6
        '7' -> 7
        '8' -> 8
        '9' -> 9
        'T' -> 10
        'J' -> if (jacksWild) 1 else 11
        'Q' -> 12
        'K' -> 13
        'A' -> 14
        else -> 0
    }
}

enum class HandType(val rank: Int) {
    FIVE_OF_A_KIND(7),
    FOUR_OF_A_KIND(6),
    FULL_HOUSE(5),
    THREE_OF_A_KIND(4),
    TWO_PAIR(3),
    ONE_PAIR(2),
    HIGH_CARD(1)
}

fun main() {
    fun part1(input: List<String>): Int {
        val handToWager = input.map{line ->
            val (handStr, wagerStr) = line.split(" ")
            Hand(handStr.toList()) to wagerStr.toInt()
        }
        var rank = 1
        return handToWager.sortedBy { it.first }.sumOf { (_, wager) ->
            wager * rank++
        }
    }

    fun part2(input: List<String>): Int {
        val handToWager = input.map{line ->
            val (handStr, wagerStr) = line.split(" ")
            Hand(handStr.toList(), true) to wagerStr.toInt()
        }
        var rank = 1
        return handToWager.sortedBy { it.first }.sumOf { (_, wager) ->
            wager * rank++
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    val test2Input = readInput("Day07_test2")
    check(part2(test2Input) == 6839)

    val input = readInput("Day07")
    part1(input).println() // 247815719
    part2(input).println() // 248778665 too high
}
