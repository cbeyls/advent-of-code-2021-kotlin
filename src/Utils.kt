import java.io.File

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()


/**
 * Matrix utilities
 */

data class Position(val x: Int, val y: Int)

operator fun Array<IntArray>.get(position: Position): Int = this[position.x][position.y]
operator fun Array<IntArray>.set(position: Position, value: Int) {
    this[position.x][position.y] = value
}

val Array<IntArray>.allPositions: Sequence<Position>
    get() = sequence {
        for (x in indices) {
            for (y in this@allPositions[x].indices) {
                yield(Position(x, y))
            }
        }
    }

fun Array<IntArray>.adjacentPositions(position: Position): Sequence<Position> = sequence {
    val (x, y) = position
    if (x > 0) yield(Position(x - 1, y))
    if (x < lastIndex) yield(Position(x + 1, y))
    if (y > 0) yield(Position(x, y - 1))
    if (y < this@adjacentPositions[x].lastIndex) yield(Position(x, y + 1))
}

// Includes diagonal positions
fun Array<IntArray>.surroundingPositions(position: Position): Sequence<Position> = sequence {
    val (x, y) = position
    for (i in (x - 1).coerceAtLeast(0)..(x + 1).coerceAtMost(lastIndex)) {
        for (j in (y - 1).coerceAtLeast(0)..(y + 1).coerceAtMost(this@surroundingPositions[x].lastIndex)) {
            if (i != x || j != y) {
                yield(Position(i, j))
            }
        }
    }
}