fun main() {
    println("part 1: " + totalSyntaxErrorScore())
    println("part 2: " + middleCompletionScore())
}

private val OPENING_CHARS = charArrayOf('(', '[', '{', '<')
private val CLOSING_CHARS = charArrayOf(')', ']', '}', '>')
private val SCORES = intArrayOf(3, 57, 1197, 25137)

private fun getSyntaxErrorScore(line: String): Int {
    val expectedChars = mutableListOf<Char>()
    for (c in line) {
        val openingIndex = OPENING_CHARS.indexOf(c)
        if (openingIndex >= 0) {
            // Opening char
            expectedChars += CLOSING_CHARS[openingIndex]
        } else {
            // Closing char
            if (c != expectedChars.removeLast()) {
                return SCORES[CLOSING_CHARS.indexOf(c)]
            }
        }
    }
    return 0   // No error found
}

private fun totalSyntaxErrorScore(): Int {
    return readInput("Day10").sumOf { getSyntaxErrorScore(it) }
}

private fun getCompletionScore(line: String): Long {
    val expectedIndexes = mutableListOf<Int>()
    for (c in line) {
        val openingIndex = OPENING_CHARS.indexOf(c)
        if (openingIndex >= 0) {
            expectedIndexes += openingIndex
        } else {
            expectedIndexes.removeLast()
        }
    }
    return expectedIndexes.foldRight(0L) { index, score -> score * 5 + index + 1 }
}

private fun middleCompletionScore(): Long {
    return readInput("Day10")
        .filter { getSyntaxErrorScore(it) == 0 }
        .map { getCompletionScore(it) }
        .sorted()
        .let { scores -> scores[scores.size / 2] }
}