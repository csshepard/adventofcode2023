
data class ScratchCard(
    val id: Int,
    val winners: Set<Int>,
    val numbers: Set<Int>,
    var count: Int = 1
) {
    val matchCount: Int get() = numbers.intersect(winners).size
    val score: Int get() {
        val matches = matchCount
        return if (matches == 0) {
            0
        } else {
            1 shl (matches-1)
        }
    }

    companion object {
        fun parseLine(line: String): ScratchCard {
            val (idPart, rem) = line.split(":", limit=2)
            val id = idPart.split(" ").last().toInt()
            val (winnerPart, numberPart) = rem.split("|", limit=2)
            val winners = winnerPart.trim().split(Regex("""\s+""")).map{it.toInt()}.toSet()
            val numbers = numberPart.trim().split(Regex("""\s+""")).map{it.toInt()}.toSet()
            return ScratchCard(id, winners, numbers)
        }
    }
}
fun main() {
    fun part1(input: List<String>): Int {
        return input.map{ScratchCard.parseLine(it)}.sumOf{it.score}
    }

    fun part2(input: List<String>): Int {
        val originalCards = input.map{ScratchCard.parseLine(it)}
        originalCards.forEachIndexed {index, card ->
            val matches = card.matchCount
            for(i in 1..matches) {
                originalCards.getOrNull(index+i)?.let{
                    it.count += card.count
                }
            }
        }
        return originalCards.sumOf{it.count}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    val test2Input = readInput("Day04_test2")
    check(part2(test2Input) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
