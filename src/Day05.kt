fun main() {
    println("part 1: " + countIntersections(false))
    println("part 2: " + countIntersections(true))
}

private data class Segment(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
    val isHorizontal: Boolean = y1 == y2
    val isVertical: Boolean = x1 == x2

    fun draw(matrix: MutableMatrix) {
        val stepX = getStep(x1, x2)
        val stepY = getStep(y1, y2)
        var x = x1
        var y = y1
        while (true) {
            matrix[x, y]++
            if (x == x2 && y == y2) {
                break
            }
            x += stepX
            y += stepY
        }
    }
}

private fun getStep(a: Int, b: Int): Int = when {
    a < b -> 1
    b < a -> -1
    else -> 0
}

private class MutableMatrix(val width: Int, val height: Int) {
    val values = IntArray(width * height)

    operator fun get(x: Int, y: Int): Int {
        return values[(x * height) + y]
    }

    operator fun set(x: Int, y: Int, value: Int) {
        values[(x * height) + y] = value
    }
}

private fun readSegments(): List<Segment> {
    return readInput("Day05")
        .map {
            val (start, end) = it.split(" -> ")
            val (x1, y1) = start.split(",")
            val (x2, y2) = end.split(",")
            Segment(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
        }
}

private fun countIntersections(includeDiagonals: Boolean): Int {
    val segments = readSegments()
    val width = segments.maxOf { maxOf(it.x1, it.x2) } + 1
    val height = segments.maxOf { maxOf(it.y1, it.y2) } + 1
    val matrix = MutableMatrix(width, height)
    segments
        .let { list ->
            if (includeDiagonals) {
                list
            } else {
                list.filter { it.isHorizontal || it.isVertical }
            }
        }
        .forEach { it.draw(matrix) }
    return matrix.values.count { it > 1 }
}