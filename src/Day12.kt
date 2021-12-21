fun main() {
    println("part 1: " + totalSimplePaths())
    println("part 2: " + totalComplexPaths())
}

private class Cave(val name: String) {
    val paths = mutableSetOf<Cave>()
    val isSmall
        get() = name[0].isLowerCase()

    companion object {
        const val START_NAME = "start"
        const val END_NAME = "end"
    }
}

private fun readCaveGraph(): Cave {
    val caves = mutableMapOf<String, Cave>()
    readInput("Day12")
        .forEach { line ->
            val (first, second) = line.split("-")
            val cave1 = caves.getOrPut(first) { Cave(first) }
            val cave2 = caves.getOrPut(second) { Cave(second) }
            cave1.paths += cave2
            cave2.paths += cave1
        }
    return caves.getValue(Cave.START_NAME)
}

private fun countPathsFromCave(cave: Cave, visitedCaves: Set<Cave>, allowSecondCaveVisit: Boolean): Int {
    if (cave.name == Cave.END_NAME) return 1
    var newAllowSecondCaveVisit = allowSecondCaveVisit
    if (cave.isSmall && visitedCaves.contains(cave)) {
        if (!allowSecondCaveVisit || cave.name == Cave.START_NAME) return 0  // forbidden
        newAllowSecondCaveVisit = false
    }
    return cave.paths.sumOf { countPathsFromCave(it, visitedCaves + cave, newAllowSecondCaveVisit) }
}

private fun totalSimplePaths(): Int {
    val caveGraph = readCaveGraph()
    return countPathsFromCave(caveGraph, emptySet(), false)
}

private fun totalComplexPaths(): Int {
    val caveGraph = readCaveGraph()
    return countPathsFromCave(caveGraph, emptySet(), true)
}