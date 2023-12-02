import java.lang.NumberFormatException

fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        val numberRegex = Regex("""^\D*(\d).*?(\d)?\D*${'$'}""")
        input.forEach { line ->
            val matchResult = numberRegex.matchEntire(line)
            if (matchResult == null) {
                println("Couldn't find numbers in $line")
            } else {
                val numString = if (matchResult.groupValues[2].isEmpty()) {
                    matchResult.groupValues[1] + matchResult.groupValues[1]
                } else {
                    matchResult.groupValues[1] + matchResult.groupValues[2]
                }
                sum += numString.toInt()
            }
        }
        return sum
    }

    val numMap = mapOf(
        "1" to 1,
        "one" to 1,
        "2" to 2,
        "two" to 2,
        "3" to 3,
        "three" to 3,
        "4" to 4,
        "four" to 4,
        "5" to 5,
        "five" to 5,
        "6" to 6,
        "six" to 6,
        "7" to 7,
        "seven" to 7,
        "8" to 8,
        "eight" to 8,
        "9" to 9,
        "nine" to 9
    )
    fun String.toNumber(): Int {
        return numMap.getOrElse(this) {throw NumberFormatException("$this is not a number")
        }
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        val numberRegex = Regex("""\d|one|two|three|four|five|six|seven|eight|nine""")
        input.forEach { line ->
            val firstMatch = numberRegex.find(line)
            if (firstMatch != null) {
                sum += firstMatch.value.toNumber() * 10
            } else {
                println("Couldn't find a number in $line")
            }
            for (index in line.length-1 downTo 0 step 1) {
                val lastMatch = numberRegex.find(line, index)
                if (lastMatch != null) {
                    sum += lastMatch.value.toNumber()
                    break
                }
            }
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val test2Input = readInput("Day01_test2")
    check(part2(test2Input) == 342)

    val input = readInput("Day01")
    part1(input).let {
        it.println()
        check(it == 53386)
    }
    part2(input).let{
        it.println()
        check(it == 53312)
    }
}
