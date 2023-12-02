import kotlin.math.max

data class Game(
    val id: Int,
    val red: Int,
    val green: Int,
    val blue: Int
) {

    fun checkValid(other: Game): Boolean {
        return red<=other.red && green<=other.green && blue<=other.blue
    }
    fun power(): Int {
        return red*green*blue
    }
    companion object {
        fun parseLine(line: String): Game {
            val id = line.substring(5).split(":")[0].toInt()
            val pulls = line.split(":")[1].split(";")
            var maxRed = 0
            var maxGreen = 0
            var maxBlue = 0
            pulls.forEach { pull ->
                pull.split(",").forEach{group ->
                    val (countS: String,  color: String) = group.trim().split(" ")
                    val count = countS.toInt()
                    when (color.trim()) {
                        "green" -> maxGreen = max(maxGreen, count)
                        "blue" -> maxBlue = max(maxBlue, count)
                        "red" -> maxRed = max(maxRed, count)
                    }
                }
            }
            return Game(id, maxRed, maxGreen, maxBlue)
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val testGame = Game(0, 12,13,14)
        val games = input.map{line -> Game.parseLine(line)}
        return games.filter { it.checkValid(testGame) }.fold(0) { r, n -> r+n.id}
    }

    fun part2(input: List<String>): Int {
        val games = input.map{line -> Game.parseLine(line)}
        return games.fold(0) {r, n -> r+n.power()}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    val test2Input = readInput("Day02_test2")
    check(part2(test2Input) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
