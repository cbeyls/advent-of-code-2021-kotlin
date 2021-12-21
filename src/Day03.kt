fun main() {
    println("part 1: " + part1())
    println("part 2: " + part2())
}

private fun readBytes(): List<String> {
    return readInput("Day03")
}

private fun part1(): Int {
    val measurements = readBytes()
    val gamma = (0 until measurements.first().length).map { index ->
        measurements.count { value -> value[index] == '1' }.let { if (it > measurements.size - it) '1' else '0' }
    }
    val epsilon = gamma.map { if (it != '0') '1' else '0' }
    return gamma.toCharArray().concatToString().toInt(2) * epsilon.toCharArray().concatToString().toInt(2)
}

private fun part2(): Int {
    val measurements = readBytes()
    val oxygenRating = getBitValue(MOST_COMMON_BIT_MATCHER, measurements, 0)
    val co2ScrubberRating = getBitValue(LEAST_COMMON_BIT_MATCHER, measurements, 0)
    return oxygenRating * co2ScrubberRating
}

private val MOST_COMMON_BIT_MATCHER = { value: Char, mostCommonBit: Char -> value == mostCommonBit }
private val LEAST_COMMON_BIT_MATCHER = { value: Char, mostCommonBit: Char -> value != mostCommonBit }

private tailrec fun getBitValue(
    bitMatcher: (value: Char, mostCommonBit: Char) -> Boolean,
    measurements: List<String>,
    index: Int
): Int {
    if (measurements.size == 1) {
        return measurements.first().toInt(2)
    }
    val mostCommonBit = measurements.count { value -> value[index] == '1' }
        .let { if (it >= measurements.size - it) '1' else '0' }
    return getBitValue(bitMatcher, measurements.filter { value -> bitMatcher(value[index], mostCommonBit) }, index + 1)
}