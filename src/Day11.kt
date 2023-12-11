
class Universe(input: List<String>) {
    private val galaxies: List<Pair<Int,Int>>
    private val emptyCols: Set<Int>
    private val emptyRows: Set<Int>

    init {
        val populatedRows = mutableSetOf<Int>()
        val populatedCols = mutableSetOf<Int>()
        galaxies = input.mapIndexed { row, line ->
            line.mapIndexedNotNull { col, symbol ->
                if (symbol == '#') {
                    populatedRows.add(row)
                    populatedCols.add(col)
                    col to row
                } else {
                    null
                }
            }
        }.flatten()
        emptyCols = input[0].indices.subtract(populatedCols)
        emptyRows = input.indices.subtract(populatedRows)
    }

    fun getDistances(expansionSize: Long = 2L): List<Long> {
        return galaxies.mapIndexed { index, galaxy ->
            galaxies.subList(index+1, galaxies.size).map { galaxyOther ->
                val (colLeft, colRight) = listOf(galaxy.first, galaxyOther.first).sorted()
                val (rowLeft, rowRight) = listOf(galaxy.second, galaxyOther.second).sorted()
                val emptyColCount = emptyCols.filter{ it in (colLeft + 1)..<colRight }.size
                val emptyRowCount = emptyRows.filter{ it in (rowLeft + 1)..<rowRight }.size
                (colRight - colLeft) + (rowRight - rowLeft) + (emptyRowCount * expansionSize - emptyRowCount) + ( emptyColCount * expansionSize - emptyColCount)
            }
        }.flatten()
    }
}
fun main() {
    fun part1(input: List<String>): Long {
        return Universe(input).getDistances().sum()
    }

    fun part2(input: List<String>): Long {
        return Universe(input).getDistances(1000000).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374L)
    check(part2(testInput) == 82000210L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
