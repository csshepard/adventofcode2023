
data class FarmMaps(
    val seedToSoilMap: List<RangeMapper>,
    val soilToFertilizerMap: List<RangeMapper>,
    val fertilizerToWaterMap: List<RangeMapper>,
    val waterToLightMap: List<RangeMapper>,
    val lightToTempMap: List<RangeMapper>,
    val tempToHumidityMap: List<RangeMapper>,
    val humidityToLocationMap: List<RangeMapper>
) {
    fun getLocationFromSeed(seed: Long): Long {
        return seed.mapValue(seedToSoilMap)
            .mapValue(soilToFertilizerMap)
            .mapValue(fertilizerToWaterMap)
            .mapValue(waterToLightMap)
            .mapValue(lightToTempMap)
            .mapValue(tempToHumidityMap)
            .mapValue(humidityToLocationMap)
    }
}

fun Long.mapValue(map: List<RangeMapper>): Long {
    map.forEach {
        if (it.inRange(this)) {
            return it.mapValue(this)
        }
    }
    return this
}

data class RangeMapper(
    val origin: Long,
    val offset: Long,
    val length: Long
) {
    fun inRange(value: Long): Boolean {
        return value >= origin && value <= origin+length
    }
    fun mapValue(value: Long): Long {
        if (inRange(value)) {
            return value + offset
        }
        return value
    }
    companion object {
        fun parseLine(line: String): RangeMapper {
            line.split(" ").let { (destinationString, sourceString, lengthString) ->
                val source = sourceString.toLong()
                return RangeMapper(source, destinationString.toLong() - source, lengthString.toLong())
            }
        }
    }
}

data class Almanac(
    val seeds: List<Long>,
    val farmMaps: FarmMaps
) {
    companion object {
        fun parseInput(input: List<String>): Almanac {
            var parseState = ParseState.SEEDS
            val seeds = mutableListOf<Long>()
            val ssmap = mutableListOf<RangeMapper>()
            val sfmap = mutableListOf<RangeMapper>()
            val fwmap = mutableListOf<RangeMapper>()
            val wlmap = mutableListOf<RangeMapper>()
            val ltmap = mutableListOf<RangeMapper>()
            val thmap = mutableListOf<RangeMapper>()
            val hlmap = mutableListOf<RangeMapper>()
            input.forEach { line ->
                if (line.isNotBlank()) {
                    when (parseState) {
                        ParseState.SEEDS -> {
                            if (line.startsWith("seeds:")) {
                                seeds.addAll(line.substring(7).split(" ").map { it.toLong() })
                            } else {
                                parseState = parseState.getNextState()
                            }
                        }
                        ParseState.SSMAP -> {
                            if (line[0].isDigit()) {
                                ssmap.add(RangeMapper.parseLine(line))
                            } else {
                                parseState = parseState.getNextState()
                            }
                        }
                        ParseState.SFMAP -> {
                            if (line[0].isDigit()) {
                                sfmap.add(RangeMapper.parseLine(line))
                            } else {
                                parseState = parseState.getNextState()
                            }
                        }
                        ParseState.FWMAP -> {
                            if (line[0].isDigit()) {
                                fwmap.add(RangeMapper.parseLine(line))
                            } else {
                                parseState = parseState.getNextState()
                            }
                        }
                        ParseState.WLMAP -> {
                            if (line[0].isDigit()) {
                                wlmap.add(RangeMapper.parseLine(line))
                            } else {
                                parseState = parseState.getNextState()
                            }
                        }
                        ParseState.LTMAP -> {
                            if (line[0].isDigit()) {
                                ltmap.add(RangeMapper.parseLine(line))
                            } else {
                                parseState = parseState.getNextState()
                            }
                        }
                        ParseState.THMAP -> {
                            if (line[0].isDigit()) {
                                thmap.add(RangeMapper.parseLine(line))
                            } else {
                                parseState = parseState.getNextState()
                            }
                        }
                        ParseState.HLMAP -> {
                            if (line[0].isDigit()) {
                                hlmap.add(RangeMapper.parseLine(line))
                            } else {
                                parseState = parseState.getNextState()
                            }
                        }
                        ParseState.INVALID -> {

                        }
                    }
                }
            }
            return Almanac(seeds, FarmMaps(ssmap, sfmap, fwmap, wlmap, ltmap, thmap, hlmap))
        }
    }
}

enum class ParseState {
    SEEDS,
    SSMAP,
    SFMAP,
    FWMAP,
    WLMAP,
    LTMAP,
    THMAP,
    HLMAP,
    INVALID;

    fun getNextState(): ParseState {
        return when (this) {
            SEEDS -> SSMAP
            SSMAP -> SFMAP
            SFMAP -> FWMAP
            FWMAP -> WLMAP
            WLMAP -> LTMAP
            LTMAP -> THMAP
            THMAP -> HLMAP
            HLMAP, INVALID -> INVALID
        }
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        val almanac = Almanac.parseInput(input)
        return almanac.seeds.minOfOrNull { almanac.farmMaps.getLocationFromSeed(it) }?:-1
    }

    fun part2(input: List<String>): Long {
        val almanac = Almanac.parseInput(input)
        return almanac.seeds.chunked(2).minOfOrNull { (start, length) ->
            // Very brute force, maybe I'll come up with a better solution later, but probably not.
            (start until start + length).minOfOrNull { almanac.farmMaps.getLocationFromSeed(it) } ?: -1
        }?:-1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    val test2Input = readInput("Day05_test2")
    check(part2(test2Input) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
