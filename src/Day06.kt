fun main() {
    println("part 1: " + countFishes(80))
    println("part 2: " + countFishes(256))
}

private fun countFishes(days: Int): Long {
    val fishes = readInput("Day06")
        .first()
        .split(",")
        .map { it.toInt() }
    val fishCounts = LongArray(9)
    for (fish in fishes) {
        fishCounts[fish]++
    }
    repeat (days) {
        val newFishes = fishCounts[0]
        // Shift all generations left
        for (i in 0..7) {
            fishCounts[i] = fishCounts[i + 1]
        }
        fishCounts[6] += newFishes
        fishCounts[8] = newFishes
    }
    return fishCounts.sum()
}