
fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day00_test")
    check(part1(testInput) == 0)
    val test2Input = readInput("Day00_test2")
    check(part2(test2Input) == 0)

    val input = readInput("Day00")
    part1(input).println()
    part2(input).println()
}
