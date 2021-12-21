fun main() {
    println("part 1: " + part1())
    println("part 2: " + part2())
}

private class BingoBoard(private val rows: List<List<Int>>) {
    fun isWinning(winningNumbers: Set<Int>): Boolean {
        return rows.any { row -> row.all { it in winningNumbers } }
                || (0 until BOARD_SIZE).any { columnIndex -> rows.all { it[columnIndex] in winningNumbers } }
    }

    fun getScore(winningNumbers: Set<Int>): Int {
        var total = 0
        for (row in rows) {
            for (number in row) {
                if (number !in winningNumbers) {
                    total += number
                }
            }
        }
        return total
    }
}

private const val BOARD_SIZE = 5

private fun readBingoBoards(stream: Sequence<String>): List<BingoBoard> {
    return stream.chunked(BOARD_SIZE + 1) { lines ->
        // Drop the first line of each board which is empty
        val numbers = lines.drop(1).map { line ->
            line.split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        }
        BingoBoard(numbers)
    }.toList()
}

private data class BingoSetup(val numbers: List<Int>, val boards: List<BingoBoard>)

private fun readBingoSetup(): BingoSetup {
    val lines = readInput("Day04")
    val numbers = lines.first().split(",").map { it.toInt() }
    return BingoSetup(
        numbers = numbers,
        boards = readBingoBoards(lines.asSequence().drop(1))
    )
}

private fun part1(): Int {
    val (numbers, boards) = readBingoSetup()
    val winningNumbers = mutableSetOf<Int>()
    for (number in numbers) {
        winningNumbers += number
        boards.firstOrNull { it.isWinning(winningNumbers) }?.let { winningBoard ->
            return winningBoard.getScore(winningNumbers) * number
        }
    }
    // No board won
    return 0
}

private fun part2(): Int {
    val setup = readBingoSetup()
    val numbers = setup.numbers
    var boards = setup.boards
    val winningNumbers = mutableSetOf<Int>()
    for (number in numbers) {
        winningNumbers += number
        for (board in boards) {
            if (board.isWinning(winningNumbers)) {
                if (boards.size == 1) {
                    return board.getScore(winningNumbers) * number
                }
                boards -= board
            }
        }
    }
    // No board won
    return 0
}