fun main() {
    println("part 1: " + part1())
    println("part 2: " + part2())
}

private fun part1(): Int {
    return readInput("Day01")
        .map { it.toInt() }
        .windowed(2)
        .count { (a, b) ->
            b > a
        }
}

private fun part2(): Int {
    return readInput("Day01")
        .map { it.toInt() }
        .windowed(4)
        .count { (a, _, _, d) ->
            d > a
        }
}