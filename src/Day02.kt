fun main() {
    println("part 1: " + part1())
    println("part 2: " + part2())
}

data class State(
    val position: Int = 0,
    val depth: Int = 0,
    val aim: Int = 0
)

sealed interface Command {
    class Forward(val value: Int) : Command
    class Down(val value: Int) : Command
    class Up(val value: Int) : Command
}

private fun readCommands(): List<Command> {
    return readInput("Day02")
        .map { line ->
            val (label, value) = line.split(" ")
            when (label) {
                "forward" -> Command.Forward(value.toInt())
                "down" -> Command.Down(value.toInt())
                "up" -> Command.Up(value.toInt())
                else -> throw IllegalArgumentException("Invalid input data")
            }
        }
}

private fun part1(): Int {
    return readCommands()
        .fold(State()) { state, command ->
            when (command) {
                is Command.Down -> state.copy(depth = state.depth + command.value)
                is Command.Up -> state.copy(depth = state.depth - command.value)
                is Command.Forward -> state.copy(position = state.position + command.value)
            }
        }.let {
            it.position * it.depth
        }
}

private fun part2(): Int {
    return readCommands()
        .fold(State()) { state, command ->
            when (command) {
                is Command.Down -> state.copy(aim = state.aim + command.value)
                is Command.Up -> state.copy(aim = state.aim - command.value)
                is Command.Forward -> state.copy(
                    position = state.position + command.value,
                    depth = state.depth + state.aim * command.value
                )
            }
        }.let {
            it.position * it.depth
        }
}