
data class Engine(
    val partNumbers: MutableList<PartNumber> = mutableListOf(),
    val gearIcons: MutableSet<Pair<Int,Int>> = mutableSetOf(),
    val otherIcons: MutableSet<Pair<Int,Int>> = mutableSetOf()
) {
    private val partIcons get() = gearIcons.union(otherIcons)

    fun getValidPartIds():List<Int> {
        return partNumbers.filter{partNumber ->
            partNumber.borders.intersect(partIcons).isNotEmpty()
        }.map{it.id}
    }

    fun getValidGearRatios():List<Int> {
        return gearIcons.mapNotNull{gearIcon ->
            val adjacentPartNumbers = partNumbers.filter{it.borders.contains(gearIcon)}
            if (adjacentPartNumbers.size == 2) {
                adjacentPartNumbers[0].id * adjacentPartNumbers[1].id
            } else {
                null
            }
        }
    }
    companion object {
        fun parseInput(input: List<String>): Engine {
            val engine = Engine()
            input.forEachIndexed { index, line ->
                var partNumber = ""
                var numberStart: Int? = null
                line.forEachIndexed { charIndex, char ->
                    if (char.isDigit()) {
                        if (numberStart == null) {
                            numberStart = charIndex
                            partNumber = char.toString()
                        } else {
                            partNumber += char
                        }
                    } else {
                        numberStart?.let {
                            engine.partNumbers.add(PartNumber.generatePart(partNumber, index to it))
                            partNumber = ""
                            numberStart = null
                        }
                        if (char == '*') {
                            engine.gearIcons.add(index to charIndex)
                        } else if (char != '.') {
                            engine.otherIcons.add(index to charIndex)
                        }
                    }
                }
                numberStart?.let {
                    engine.partNumbers.add(PartNumber.generatePart(partNumber, index to it))
                }
            }
            return engine
        }
    }
}

data class PartNumber(
    val id: Int,
    val borders: Set<Pair<Int,Int>>
) {
    companion object {
        fun generatePart(idString: String, startPosition: Pair<Int, Int>): PartNumber {
            val id = idString.toInt()
            val row = startPosition.first
            val col = startPosition.second
            val borders = mutableSetOf(
                row to col-1,
                row to col+idString.length
            )
            for (i in -1 .. idString.length) {
                borders.add(row-1 to col+i)
                borders.add(row+1 to col+i)
            }
            borders.removeIf{ it.first < 0 || it.second < 0}
            return PartNumber(id, borders)
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val engine = Engine.parseInput(input)
        return engine.getValidPartIds().sum()

    }

    fun part2(input: List<String>): Int {
        val engine = Engine.parseInput(input)
        return engine.getValidGearRatios().sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    val test2Input = readInput("Day03_test2")
    check(part2(test2Input) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
