fun getNextValue(values: List<Int>): Int {
    if (values.all{it == 0}) {
        return 0
    }
    val diffs = mutableListOf<Int>()
    for (i in 0..<(values.size - 1)) {
        diffs.add(values[i+1] - values[i])
    }
    return values.last() + getNextValue(diffs)
}

fun getPrevValue(values: List<Int>): Int {
    if (values.all{it == 0}) {
        return 0
    }
    val diffs = mutableListOf<Int>()
    for (i in 0..<(values.size - 1)) {
        diffs.add(values[i+1] - values[i])
    }
    return values.first() - getPrevValue(diffs)
}
fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            getNextValue(line.split(" ").map{it.toInt()})
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            getPrevValue(line.split(" ").map{it.toInt()})
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
