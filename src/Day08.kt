fun main() {
    println("part 1: " + countUniqueSizeDigits())
    println("part 2: " + sumOfDecodedNumbers())
}

private class Reading(val signalPatterns: List<String>, val outputValue: List<String>)

private fun readInput(): List<Reading> {
    return readInput("Day08")
        .map { line ->
            val (signalPatterns, outputValue) = line.split(" | ")
            Reading(signalPatterns.split(" "), outputValue.split(" "))
        }
}

private val UNIQUE_DIGIT_SIZES = listOf(
    1 to 2,
    4 to 4,
    7 to 3,
    8 to 7
)

private fun countUniqueSizeDigits(): Int {
    return readInput()
        .sumOf { reading ->
            reading.outputValue.count { outputValue -> UNIQUE_DIGIT_SIZES.any { outputValue.length == it.second } }
        }
}

private fun extractDigitPatterns(signalPatterns: List<String>): List<Set<Char>> {
    val patterns = signalPatterns.map { it.toSet() }
    val digitPatterns = Array<Set<Char>>(10) { emptySet() }
    // start with unique size digits
    for (uniqueDigitSize in UNIQUE_DIGIT_SIZES) {
        digitPatterns[uniqueDigitSize.first] = patterns.first { it.size == uniqueDigitSize.second }
    }
    // 6 digits
    digitPatterns[6] = patterns.first { it.size == 6 && !it.containsAll(digitPatterns[1]) }
    digitPatterns[9] = patterns.first { it.size == 6 && it.containsAll(digitPatterns[4]) }
    digitPatterns[0] = patterns.first { it.size == 6 && it != digitPatterns[6] && it != digitPatterns[9] }
    // 5 digits
    digitPatterns[3] = patterns.first { it.size == 5 && it.containsAll(digitPatterns[1]) }
    digitPatterns[5] = patterns.first { it.size == 5 && digitPatterns[6].containsAll(it) }
    digitPatterns[2] = patterns.first { it.size == 5 && it != digitPatterns[3] && it != digitPatterns[5] }
    return digitPatterns.toList()
}

private fun sumOfDecodedNumbers(): Int {
    return readInput().sumOf { reading ->
        val digitPatterns = extractDigitPatterns(reading.signalPatterns)
        reading.outputValue.fold(0) { acc, display ->
            val digit = digitPatterns.indexOfFirst { digitPattern ->
                display.length == digitPattern.size && display.all { it in digitPattern }
            }
            if (digit < 0) throw IllegalStateException("Digit not found")
            acc * 10 + digit
        }.toInt()
    }
}