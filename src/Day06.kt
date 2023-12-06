
// Hard coding input since it's so short today
val testInput = listOf(7 to 9, 15 to 40, 30 to 200)
val testInput2 = listOf(71530L to 940200L)
val realInput = listOf(58 to 434, 81 to 1041, 96 to 2219, 76 to 1218)
val realInput2 = listOf(58819676L to 434104122191218L)

fun main() {
    fun part1(input: List<Pair<Int,Int>>): Int {
        return input.map{ (time, distance) ->
            (1 until time).map { chargeTime ->
                chargeTime * (time - chargeTime)
            }.count { it > distance }
        }.reduce { acc, count ->
            acc * count
        }
    }

    fun part2(input: List<Pair<Long,Long>>): Int {
        return input.map{ (time, distance) ->
            (1 until time).map { chargeTime ->
                chargeTime * (time - chargeTime)
            }.count { it > distance }
        }.reduce { acc, count ->
            acc * count
        }
    }

    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 288)
    check(part2(testInput2) == 71503)

    part1(realInput).println()
    part2(realInput2).println()
}
