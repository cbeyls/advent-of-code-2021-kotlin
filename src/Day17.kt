fun main() {
    println("part 1: " + maxY())
    println("part 2: " + solutionsCount())
}

private fun String.toIntRange() = split("..").let { (start, end) -> start.toInt()..end.toInt() }

private fun readTargetArea(): TargetArea {
    return readInput("Day17")
        .first()
        .removePrefix("target area: ")
        .split(", ")
        .let { (xRange, yRange) ->
            TargetArea(
                xRange = xRange.removePrefix("x=").toIntRange(),
                yRange = yRange.removePrefix("y=").toIntRange()
            )
        }
}

private data class TargetArea(val xRange: IntRange, val yRange: IntRange) {
    operator fun contains(position: Position) = position.x in xRange && position.y in yRange
    fun isReachableFrom(position: Position) = position.x <= xRange.last && position.y >= yRange.first
}

private val INITIAL_POSITION = Position(0, 0)

private data class Velocity(val x: Int, val y: Int)

/**
 * Returns the highest y position if the probe ends up in the target area, or null if it doesn't
 */
private fun matchesTargetArea(initialVelocity: Velocity, targetArea: TargetArea): Int? {
    var position = INITIAL_POSITION
    var maxY = position.y
    var (xVelocity, yVelocity) = initialVelocity
    while (targetArea.isReachableFrom(position)) {
        position = Position(position.x + xVelocity, position.y + yVelocity)
        maxY = maxOf(maxY, position.y)

        if (position in targetArea) {
            return maxY
        }

        xVelocity = when {
            xVelocity > 0 -> xVelocity - 1
            xVelocity < 0 -> xVelocity + 1
            else -> 0
        }
        yVelocity--
    }
    return null
}

private fun createInitialVelocitiesSequence(targetArea: TargetArea): Sequence<Velocity> = sequence {
    for (xVelocity in 1..targetArea.xRange.last) {
        for (yVelocity in targetArea.yRange.first..100) {
            yield(Velocity(xVelocity, yVelocity))
        }
    }
}

private fun maxY(): Int {
    val targetArea = readTargetArea()
    return createInitialVelocitiesSequence(targetArea).mapNotNull { matchesTargetArea(it, targetArea) }
        .maxOf { it }
}

private fun solutionsCount(): Int {
    val targetArea = readTargetArea()
    return createInitialVelocitiesSequence(targetArea).count { matchesTargetArea(it, targetArea) != null }
}