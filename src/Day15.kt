fun main() {
    println("part 1: " + shortestDistanceFromReducedMap())
    println("part 2: " + shortestDistanceFromFullMap())
}

private fun readWeights(): Array<IntArray> {
    return readInput("Day15")
        .map { line -> IntArray(line.length) { line[it].digitToInt() } }
        .toTypedArray()
}

// Using Dijkstra's algorithm
private fun getShortestDistance(weights: Array<IntArray>): Int {
    // Set all initial distances to MAX_VALUE
    val distances = Array(weights.size) { x -> IntArray(weights[x].size) { Int.MAX_VALUE } }
    val startPosition = Position(0, 0)
    distances[startPosition] = 0
    val endPosition = Position(weights.lastIndex, weights[weights.lastIndex].lastIndex)

    val pendingPositions = mutableSetOf(startPosition)
    var position: Position? = startPosition
    while (position != null && position != endPosition) {
        val distance = distances[position]
        // Set the distance to 0 to signal this node is excluded from the rest of the path
        distances[position] = 0
        pendingPositions -= position
        weights.adjacentPositions(position).forEach { adjacentPosition ->
            val oldDistance = distances[adjacentPosition]
            if (oldDistance != 0) {
                val newDistance = distance + weights[adjacentPosition]
                if (newDistance < oldDistance) {
                    // update shortest distance
                    distances[adjacentPosition] = newDistance
                    pendingPositions += adjacentPosition
                }
            }
        }
        // Select the node with the shortest current distance
        position = pendingPositions.minByOrNull { distances[it] }
    }

    return distances[endPosition]
}

private fun shortestDistanceFromReducedMap(): Int {
    return getShortestDistance(readWeights())
}

private const val MAP_MULTIPLIER = 5

private fun shortestDistanceFromFullMap(): Int {
    val weights = readWeights()
    val originalWidth = weights.size
    val originalHeight = weights[0].size
    val fullWeights = Array(originalWidth * MAP_MULTIPLIER) { x2 ->
        val x = x2 % originalWidth
        val xOffset = x2 / originalWidth
        IntArray(originalHeight * MAP_MULTIPLIER) { y2 ->
            val y = y2 % originalHeight
            val yOffset = y2 / originalHeight
            val value = weights[x][y] + xOffset + yOffset
            if (value > 9) value - 9 else value
        }
    }
    return getShortestDistance(fullWeights)
}