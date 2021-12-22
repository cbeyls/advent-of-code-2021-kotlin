fun main() {
    println("part 1: " + versionsSum())
    println("part 2: " + evaluate())
}

private fun readBitsInput(): String {
    return readInput("Day16")
        .first()
        .map { it.digitToInt(16).toString(2).padStart(4, '0') }
        .joinToString("")
}

private data class BitsIterator(private val bits: String) {
    var position: Int = 0
        private set

    fun nextBits(count: Int): String {
        val endPosition = position + count
        return bits.substring(position, endPosition)
            .also { position = endPosition }
    }
}

private sealed class Packet(val version: Int) {
    abstract val value: Long

    class LiteralValue(version: Int, override val value: Long) : Packet(version)

    class Operator(version: Int, val packets: List<Packet>, private val operation: (List<Packet>) -> Long) :
        Packet(version) {
        override val value: Long
            get() = operation(packets)
    }
}

private fun decodeLiteralValue(iterator: BitsIterator, version: Int): Packet.LiteralValue {
    val numberBits = StringBuilder()
    do {
        val prefix = iterator.nextBits(1)
        numberBits.append(iterator.nextBits(4))
    } while (prefix == "1")
    val value = numberBits.toString().toLong(2)
    return Packet.LiteralValue(version, value)
}

private fun getOperation(type: Int): (List<Packet>) -> Long = when (type) {
    0 -> { packets -> packets.sumOf { it.value } }
    1 -> { packets -> packets.fold(1L) { acc, packet -> acc * packet.value } }
    2 -> { packets -> packets.minOf { it.value } }
    3 -> { packets -> packets.maxOf { it.value } }
    5 -> { (a, b) -> if (a.value > b.value) 1L else 0L }
    6 -> { (a, b) -> if (a.value < b.value) 1L else 0L }
    7 -> { (a, b) -> if (a.value == b.value) 1L else 0L }
    else -> throw IllegalArgumentException("Unknown operation")
}

private fun decodeOperator(iterator: BitsIterator, version: Int, type: Int): Packet.Operator {
    val packets = mutableListOf<Packet>()
    when (iterator.nextBits(1)) {
        "0" -> {
            val packetsLength = iterator.nextBits(15).toInt(2)
            val endPosition = iterator.position + packetsLength
            while (iterator.position < endPosition) {
                packets += decodePacket(iterator)
            }
        }
        "1" -> {
            val packetsCount = iterator.nextBits(11).toInt(2)
            repeat(packetsCount) {
                packets += decodePacket(iterator)
            }
        }
    }
    return Packet.Operator(version, packets, getOperation(type))
}

private fun decodePacket(iterator: BitsIterator): Packet {
    val version = iterator.nextBits(3).toInt(2)
    val type = iterator.nextBits(3).toInt(2)
    return if (type == 4) decodeLiteralValue(iterator, version)
    else decodeOperator(iterator, version, type)
}

private val Packet.sumOfVersions: Int
    get() = when (this) {
        is Packet.Operator -> version + packets.sumOf { it.sumOfVersions }
        else -> version
    }

private fun versionsSum(): Int {
    val iterator = BitsIterator(readBitsInput())
    return decodePacket(iterator).sumOfVersions
}

private fun evaluate(): Long {
    val iterator = BitsIterator(readBitsInput())
    return decodePacket(iterator).value
}