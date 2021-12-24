fun main() {
    println("part 1: " + magnitudeOfTotalSum())
    println("part 2: " + largestMagnitudeOfPairSums())
}

sealed class SnailfishNumber {
    abstract val magnitude: Long
    abstract fun addLeft(addedValue: Int): SnailfishNumber
    abstract fun addRight(addedValue: Int): SnailfishNumber

    operator fun plus(other: SnailfishNumber) = PairNumber(this, other)
}

data class LiteralNumber(val value: Int) : SnailfishNumber() {
    override val magnitude: Long
        get() = value.toLong()

    override fun addLeft(addedValue: Int): SnailfishNumber {
        return if (addedValue == 0) this else LiteralNumber(value + addedValue)
    }

    override fun addRight(addedValue: Int): SnailfishNumber = addLeft(addedValue)

    override fun toString() = value.toString()

    companion object {
        val ZERO = LiteralNumber(0)
    }
}

data class PairNumber(val left: SnailfishNumber, val right: SnailfishNumber) : SnailfishNumber() {
    override val magnitude: Long
        get() = 3L * left.magnitude + 2L * right.magnitude

    override fun addLeft(addedValue: Int): SnailfishNumber {
        return if (addedValue == 0) this else left.addLeft(addedValue) + right
    }

    override fun addRight(addedValue: Int): SnailfishNumber {
        return if (addedValue == 0) this else left + right.addRight(addedValue)
    }

    override fun toString() = "[$left,$right]"
}

private fun parseSnailfishNumber(reader: CharIterator): SnailfishNumber {
    val char = reader.nextChar()
    return when {
        char.isDigit() -> LiteralNumber(char.digitToInt())
        char == '[' -> {
            val left = parseSnailfishNumber(reader)
            check(reader.nextChar() == ',')
            val right = parseSnailfishNumber(reader)
            check(reader.nextChar() == ']')
            PairNumber(left, right)
        }
        else -> throw IllegalStateException("Unexpected char at this position: $char")
    }
}

private fun readSnailfisnNumbers(): List<SnailfishNumber> {
    return readInput("Day18")
        .map { parseSnailfishNumber(it.iterator()) }
}

private class ExplodeResult(val number: SnailfishNumber, val leftBubble: Int = 0, val rightBubble: Int = 0)

private fun explodeLeftMost(pair: PairNumber, depth: Int): ExplodeResult {
    if (pair.left is PairNumber) {
        val leftResult = explodeLeftMost(pair.left, depth + 1)
        if (leftResult.number != pair.left) {
            // Bubble up left, update right literal value
            return ExplodeResult(
                number = leftResult.number + pair.right.addLeft(leftResult.rightBubble),
                leftBubble = leftResult.leftBubble
            )
        }
    }
    // Left is a literal or an unexploded pair, try exploding right
    if (pair.right is PairNumber) {
        val rightResult = explodeLeftMost(pair.right, depth + 1)
        // Bubble up right, update left literal value
        return ExplodeResult(
            number = pair.left.addRight(rightResult.leftBubble) + rightResult.number,
            rightBubble = rightResult.rightBubble
        )
    }
    // Both left and right are literals
    if (depth >= 4) {
        return ExplodeResult(
            number = LiteralNumber.ZERO,
            leftBubble = (pair.left as LiteralNumber).value,
            rightBubble = (pair.right as LiteralNumber).value
        )
    }
    return ExplodeResult(pair)
}

private fun explodeLeftMost(number: SnailfishNumber): SnailfishNumber = when (number) {
    is LiteralNumber -> number
    is PairNumber -> explodeLeftMost(number, 0).number
}

private fun splitLeftMost(number: SnailfishNumber): SnailfishNumber = when (number) {
    is LiteralNumber -> if (number.value > 9) {
        val half = number.value / 2
        LiteralNumber(half) + LiteralNumber(number.value - half)
    } else number
    is PairNumber -> {
        val newLeft = splitLeftMost(number.left)
        if (newLeft != number.left) {
            newLeft + number.right
        } else {
            number.left + splitLeftMost(number.right)
        }
    }
}

private fun reduce(number: SnailfishNumber): SnailfishNumber {
    var result = number
    while (true) {
        val previousResult = result
        result = explodeLeftMost(result)
        if (result != previousResult) continue
        result = splitLeftMost(previousResult)
        if (result == previousResult) break
    }
    return result
}

private fun magnitudeOfTotalSum(): Long {
    return readSnailfisnNumbers()
        .reduce { a, b -> reduce(a + b) }
        .magnitude
}

private fun <T> List<T>.pairPermutations(): Sequence<Pair<T, T>> = sequence {
    forEachIndexed { i, first ->
        forEachIndexed { j, second ->
            if (i != j) yield(first to second)
        }
    }
}

private fun largestMagnitudeOfPairSums(): Long {
    return readSnailfisnNumbers()
        .pairPermutations()
        .map { (a, b) -> reduce(a + b) }
        .maxOf { it.magnitude }
}