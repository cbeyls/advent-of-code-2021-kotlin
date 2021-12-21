import kotlin.math.absoluteValue

fun main() {
    println("part 1: " + getBestPosition { it })
    println("part 2: " + getBestPosition { rangeSum(it) })
}

private fun readCrabPositions(): IntArray {
    return readInput("Day07")
        .first()
        .split(",")
        .map { it.toInt() }
        .toIntArray()
}

private fun getBestPosition(fuelUnits: (steps: Int) -> Int): Int {
    val crabPositions = readCrabPositions()
    val minPosition = crabPositions.minOrNull()!!
    val maxPosition = crabPositions.maxOrNull()!!
    return (minPosition..maxPosition).asSequence()
        .map { position -> crabPositions.sumOf { fuelUnits((it - position).absoluteValue) } }
        .minOrNull()!!
}

private fun rangeSum(n: Int): Int {
    return n * (n + 1) / 2
}