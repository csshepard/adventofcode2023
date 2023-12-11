data class Pipe(private val x: Int, private val y: Int, val shape: Char) {

    val validExits: List<Direction> = when (shape) {
        '|' -> listOf(Direction.North, Direction.South)
        '-' -> listOf(Direction.West, Direction.East)
        'L' -> listOf(Direction.North, Direction.East)
        'J' -> listOf(Direction.North, Direction.West)
        '7' -> listOf(Direction.South, Direction.West)
        'F' -> listOf(Direction.South, Direction.East)
        'S' -> listOf(Direction.Start)
        else -> listOf()
    }

    fun validDirection(entryDirection: Direction): Direction {
        return validExits.firstOrNull { direction ->
            when (entryDirection) {
                Direction.North -> direction != Direction.North
                Direction.South -> direction != Direction.South
                Direction.East -> direction != Direction.East
                Direction.West -> direction != Direction.West
                Direction.Start -> true
                Direction.Invalid -> false
            }
        }?: Direction.Invalid
    }

    fun traverse(direction: Direction): Pair<Int, Int> {
        return when (direction) {
            Direction.North -> x to (y - 1)
            Direction.South -> x to (y + 1)
            Direction.East -> (x + 1) to y
            Direction.West -> (x - 1) to y
            Direction.Start, Direction.Invalid -> x to y
        }
    }

    companion object {
        fun getShapeFromExits(directions: List<Direction>): Char{
            return if (directions.contains(Direction.North)) {
                if (directions.contains(Direction.South)) {
                    '|'
                } else if (directions.contains(Direction.West)) {
                    'J'
                } else {
                    'L'
                }
            } else if (directions.contains(Direction.East)) {
                if (directions.contains(Direction.West)) {
                    '-'
                } else {
                    'F'
                }
            } else {
                '7'
            }
        }
    }
}

fun Char.isCorner(): Boolean {
    return (this == 'F' || this == 'J' || this == 'L' || this == '7')
}

enum class Direction {
    North,
    South,
    East,
    West,
    Start,
    Invalid;

    fun invertDirection(): Direction {
        return when (this) {
            North -> South
            South -> North
            East -> West
            West -> East
            Start -> Start
            Invalid -> Invalid
        }
    }
}

class Path(input: List<String>) {
    private lateinit var entryPipe: Pipe
    private val rowCount = input.size
    private val colCount = input.getOrNull(0)?.length?:0
    private val pipes = input.mapIndexed{ row, line ->
        line.mapIndexed{ col, shape ->
            val pipe = Pipe(col, row, shape)
            if (shape == 'S') {
                entryPipe = pipe
            }
            pipe
        }
    }
    val path = findPath()

    private fun findPath(): List<Pipe> {
        val path = mutableListOf<Pipe>()
        var currentPipe = entryPipe
        var validDirection = getValidDirectionsForStart().firstOrNull()?:Direction.Invalid
        do {
            path.add(currentPipe)
            val (x, y) = currentPipe.traverse(validDirection)
            currentPipe = pipes[y][x]
            validDirection = currentPipe.validDirection(validDirection.invertDirection())
        } while (currentPipe != entryPipe)
        return path
    }


    private fun getValidDirectionsForStart(): List<Direction> {
        return Direction.entries.mapNotNull {
            val (x, y) = entryPipe.traverse(it)
            if (y >= 0 && x>= 0 && x <= colCount && y <= rowCount && pipes[y][x].validExits.contains(it.invertDirection())) {
                it
            } else {
                null
            }
        }
    }

    fun getInsideLoopArea(): Int {
        var size = 0
        pipes.indices.forEach{row ->
            var isInside = false
            var lastCorner: Char? = null
            pipes[0].indices.forEach{col ->
                if (path.contains(pipes[row][col])) {
                    val pipe = pipes[row][col]
                    val shape = if (pipe.shape == 'S') Pipe.getShapeFromExits(getValidDirectionsForStart()) else pipe.shape
                    if (shape.isCorner()) {
                        if (lastCorner != null) {
                            if (lastCorner == 'F' && shape == 'J') {
                                isInside = !isInside
                            } else if (lastCorner == 'L' && shape == '7') {
                                isInside = !isInside
                            }
                            lastCorner = null
                        } else {
                            lastCorner = shape
                        }
                    } else if (shape == '|') {
                        isInside = !isInside
                    }
                } else if (isInside) {
                    size++
                }
            }
        }
        return size
    }


}


fun main() {
    fun part1(input: List<String>): Int {
        return Path(input).path.size / 2
    }

    fun part2(input: List<String>): Int {
        return Path(input).getInsideLoopArea()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 8) { println("expecting 8, got ${part1(testInput)}")}
    val test2Input = readInput("Day10_test2")
    check(part2(test2Input) == 10) { println("expecting 10, got ${part2(test2Input)}")}

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
