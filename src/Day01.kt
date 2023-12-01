import java.lang.NumberFormatException

fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        val numberRegex = Regex("""^\D*(\d).*?(\d?)\D*${'$'}""")
        input.forEach { line ->
            numberRegex.matchEntire(line).let{ matchResult ->
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


        }
        return sum
    }

    fun String.toNumber(): Int {
        return when(this) {
            "1", "one" -> 1
            "2", "two" -> 2
            "3", "three" -> 3
            "4", "four" -> 4
            "5", "five" -> 5
            "6", "six" -> 6
            "7", "seven" -> 7
            "8", "eight" -> 8
            "9", "nine" -> 9
            "0", "zero" -> 0
            else -> throw NumberFormatException("$this is not a number")
        }
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        val numberRegex = Regex("""\d|one|two|three|four|five|six|seven|eight|nine|zero""", option=RegexOption.IGNORE_CASE)
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
    part1(input).println()
    part2(input).println()
}
