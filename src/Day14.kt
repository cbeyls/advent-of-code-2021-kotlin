fun main() {
    println("part 1: " + elementsQuantity(10))
    println("part 2: " + scalableElementsQuantity(40))
}

private data class PolymerConfiguration(val template: String, val insertionRules: Map<String, Char>)

private fun readConfiguration(): PolymerConfiguration {
    val lines = readInput("Day14")
    return PolymerConfiguration(
        template = lines.first(),
        insertionRules = lines.takeLastWhile { it.isNotEmpty() }
            .associate {
                val (pair, replacement) = it.split(" -> ")
                pair to replacement.first()
            }
    )
}

private fun doStep(polymer: String, insertionRules: Map<String, Char>): String {
    val result = StringBuilder(polymer.length)
    polymer
        .windowedSequence(size = 2, partialWindows = true)
        .forEach { pair ->
            result.append(pair.first())
            insertionRules[pair]?.let { result.append(it) }
        }
    return result.toString()
}

private fun elementsQuantity(steps: Int): Int {
    val (template, insertionRules) = readConfiguration()
    var polymer = template
    repeat(steps) {
        polymer = doStep(polymer, insertionRules)
    }
    val elementQuantities = polymer.groupingBy { it }.eachCount()
    return elementQuantities.maxOf { it.value } - elementQuantities.minOf { it.value }
}

private fun scalableDoStep(
    pairs: Map<String, Long>,
    insertionRules: Map<String, Char>,
    elementQuantities: MutableMap<Char, Long>
): Map<String, Long> {
    val newPairs = mutableMapOf<String, Long>()
    for ((pair, count) in pairs) {
        val insert = insertionRules[pair]
        if (insert == null) {
            newPairs[pair] = count
        } else {
            val firstPair = String(charArrayOf(pair.first(), insert))
            val secondPair = String(charArrayOf(insert, pair.last()))
            newPairs[firstPair] = newPairs.getOrDefault(firstPair, 0L) + count
            newPairs[secondPair] = newPairs.getOrDefault(secondPair, 0L) + count
            elementQuantities[insert] = elementQuantities.getOrDefault(insert, 0L) + count
        }
    }
    return newPairs
}

private fun scalableElementsQuantity(steps: Int): Long {
    val (template, insertionRules) = readConfiguration()
    var pairs = template.windowed(2).groupingBy { it }.eachCount()
        .mapValues { (_, value) -> value.toLong() }
    val elementQuantities = template.groupingBy { it }.eachCount()
        .mapValues { (_, value) -> value.toLong() }
        .toMutableMap()
    repeat(steps) {
        pairs = scalableDoStep(pairs, insertionRules, elementQuantities)
    }
    return elementQuantities.maxOf { it.value } - elementQuantities.minOf { it.value }
}