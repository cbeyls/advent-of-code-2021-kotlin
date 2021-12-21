fun main() {
    println("part 1: " + totalFlashCount(100))
    println("part 2: " + firstSynchronizedIteration())
}

private fun readOctopusMap(): Array<IntArray> {
    return readInput("Day11")
        .map { line -> IntArray(line.length) { line[it].digitToInt() } }
        .toTypedArray()
}

private const val MAX_LEVEL = 9

private fun flashAndPropagateEnergy(octopusMap: Array<IntArray>, position: Position): Int {
    octopusMap[position] = 0
    var flashCount = 1
    octopusMap.surroundingPositions(position).forEach { adjacentPosition ->
        var value = octopusMap[adjacentPosition]
        if (value != 0) {   // Skip already processed flashes
            value++
            octopusMap[adjacentPosition] = value
            if (value > MAX_LEVEL) {
                flashCount += flashAndPropagateEnergy(octopusMap, adjacentPosition)
            }
        }
    }
    return flashCount
}

// Runs an iteration and returns the number of flashes that occurred in this iteration
private fun doIteration(octopusMap: Array<IntArray>): Int {
    for (row in octopusMap) {
        for (y in row.indices) {
            row[y]++
        }
    }
    return octopusMap.allPositions
        .filter { octopusMap[it] > MAX_LEVEL }
        .sumOf { flashAndPropagateEnergy(octopusMap, it) }
}

private fun totalFlashCount(iterations: Int): Int {
    val octopusMap = readOctopusMap()
    var flashCount = 0
    repeat(iterations) {
        flashCount += doIteration(octopusMap)
    }
    return flashCount
}

private fun firstSynchronizedIteration(): Int {
    val octopusMap = readOctopusMap()
    var iterations = 0
    while (octopusMap.any { row -> row.any { it != 0 } }) {
        doIteration(octopusMap)
        iterations++
    }
    return iterations
}