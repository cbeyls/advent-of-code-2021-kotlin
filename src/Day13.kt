fun main() {
    println("part 1: " + totalPointsAfterFirstFold())
    println("part 2:")
    printSecretCode()
}

private data class Configuration(val points: Collection<Point>, val foldings: List<Folding>)

private data class Point(val x: Int, val y: Int)

private class Folding(val axis: String, val value: Int)

private const val FOLD_PREFIX = "fold along "

private fun readConfiguration(): Configuration {
    val lines = readInput("Day13")
    return Configuration(
        points = lines.takeWhile { it.isNotEmpty() }
            .map {
                val (x, y) = it.split(",")
                Point(x.toInt(), y.toInt())
            },
        foldings = lines.takeLastWhile { it.isNotEmpty() }
            .map {
                val (axis, value) = it.substringAfter(FOLD_PREFIX).split("=")
                Folding(axis, value.toInt())
            }
    )
}

private fun foldHorizontal(input: Collection<Point>, value: Int): Collection<Point> {
    val (left, right) = input.partition { it.x < value }
    return left.toMutableSet().also { result ->
        right.mapTo(result) { it.copy(x = value + value - it.x) }
    }
}

private fun foldVertical(input: Collection<Point>, value: Int): Collection<Point> {
    val (top, bottom) = input.partition { it.y < value }
    return top.toMutableSet().also { result ->
        bottom.mapTo(result) { it.copy(y = value + value - it.y) }
    }
}

private fun fold(input: Collection<Point>, folding: Folding): Collection<Point> {
    return when (folding.axis) {
        "x" -> foldHorizontal(input, folding.value)
        "y" -> foldVertical(input, folding.value)
        else -> throw IllegalArgumentException("Unknown axis: ${folding.axis}")
    }
}

private fun totalPointsAfterFirstFold(): Int {
    val (points, foldings) = readConfiguration()
    return fold(points, foldings.first()).size
}

private fun printSecretCode() {
    val (points, foldings) = readConfiguration()
    val finalPoints = foldings.fold(points) { currentPoints, folding -> fold(currentPoints, folding) }
    val maxX = finalPoints.maxOf { it.x }
    val maxY = finalPoints.maxOf { it.y }
    for (y in 0..maxY) {
        for (x in 0..maxX) {
            print(if (Point(x, y) in finalPoints) '#' else '.')
        }
        println()
    }
}