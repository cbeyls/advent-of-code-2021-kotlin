fun main() {
    println("part 1: " + totalRiskLevel())
    println("part 2: " + biggestBasinSizes())
}

private fun readCaveMap(): Array<IntArray> {
    return readInput("Day09")
        .map { line -> IntArray(line.length) { line[it].digitToInt() } }
        .toTypedArray()
}

private fun isLowPoint(caveMap: Array<IntArray>, position: Position): Boolean {
    val point = caveMap[position]
    return caveMap.adjacentPositions(position).all { point < caveMap[it] }
}

private fun findLowPoints(caveMap: Array<IntArray>): Sequence<Position> {
    return caveMap.allPositions.filter { isLowPoint(caveMap, it) }
}

private fun totalRiskLevel(): Int {
    val caveMap = readCaveMap()
    return findLowPoints(caveMap).sumOf { position -> caveMap[position] + 1 }
}

private fun getBasinSize(caveMap: Array<IntArray>, position: Position, visitedPositions: MutableSet<Position>): Int {
    if (caveMap[position] == 9 || position in visitedPositions) return 0
    visitedPositions += position
    return 1 + caveMap.adjacentPositions(position).sumOf { getBasinSize(caveMap, it, visitedPositions) }
}

private fun biggestBasinSizes(): Int {
    val caveMap = readCaveMap()
    return findLowPoints(caveMap)
        .map { position -> getBasinSize(caveMap, position, mutableSetOf()) }
        .sortedDescending()
        .take(3)
        .reduce { a, b -> a * b }
}