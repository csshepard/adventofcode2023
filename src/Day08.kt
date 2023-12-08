
data class Node(
    val label: String,
    var leftNode: Node? = null,
    var rightNode: Node? = null
) {

    fun setNodes(left: Node?, right: Node?) {
        leftNode = left
        rightNode = right
    }
}
data class Network(
    val nodes: MutableMap<String, Node> = mutableMapOf()
) {

    private fun getStartingNodes(): List<Node> {
        return nodes.values.filter{it.label.endsWith('A')}
    }
    fun pathLength(path: List<Char>, startingNodeLabel: String = "AAA", endingTarget: String = "ZZZ"): Long {
        var currentNode = nodes[startingNodeLabel]!!
        var length = 0L
        while(true) {
            path.forEach { direction ->
                currentNode = if (direction == 'L') {
                    currentNode.leftNode!!
                } else {
                    currentNode.rightNode!!
                }
                length++
                if (currentNode.label.endsWith(endingTarget)) {
                    return length
                }
            }
        }
    }

    fun ghostPathLength(path: List<Char>) = getStartingNodes().toMutableList().map {
            pathLength(path, it.label, "Z")
        }.reduce{ acc, other -> acc lcm other}

    companion object {
        fun parseInput(input: List<String>): Network {
            val network = Network()
            input.stream().skip(2).forEach { line ->
                val (label, left, right) = line.replace(Regex("""[=(,)]"""), "").split(Regex("""\s+"""))
                if (!network.nodes.contains(label)) {
                    network.nodes[label] = Node(label)
                }
                if (!network.nodes.contains(left)) {
                    network.nodes[left] = Node(left)
                }
                if (!network.nodes.contains(right)) {
                    network.nodes[right] = Node(right)
                }
                network.nodes[label]!!.setNodes(network.nodes[left],network.nodes[right])
            }
            return network
        }
    }
}

infix fun Long.gcd(other: Long): Long {
    return if (other == 0L) this else other gcd (this % other)
}
infix fun Long.lcm(other: Long): Long {
    return (this * other) / (this gcd other)
}

fun main() {
    fun part1(input: List<String>): Long {
        val path = input[0].toList()
        val network = Network.parseInput(input)
        return network.pathLength(path)
    }

    fun part2(input: List<String>): Long {
        val path = input[0].toList()
        val network = Network.parseInput(input)
        return network.ghostPathLength(path)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 2L)
    val test2Input = readInput("Day08_test2")
    check(part2(test2Input) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
