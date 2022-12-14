import java.io.*

class FA(data: File) {
    private val stateCount: Int
    private val symbolCount: Int
    private val startState: Set<Int>
    private val endStates: MutableSet<Set<Int>> = mutableSetOf()
    private val table: MutableMap<Pair<Int, Set<Int>>, Set<Int>> = mutableMapOf()

    init {
        val reader = data.reader(Charsets.UTF_8).readLines().toMutableList()
        reader.removeFirst()
        symbolCount = reader.removeFirst().toInt()
        val tmpStartStates = reader.removeFirst().split(" ").map { it.toInt() }.toSet()
        val tmpEndStates = reader.removeFirst().split(" ").map { it.toInt() }.toSet()
        val tmpTable = reader.map { val buf = it.split(" ").map { it.toInt() }; Pair(Pair(buf[0], buf[1]), buf[2]) }
        val queue = mutableListOf(tmpStartStates)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            repeat(symbolCount) { symbol ->
                val tmp =
                    tmpTable.filter { it.first.second == symbol && it.first.first in current }.map { it.second }.toSet()
                if (!table.contains(Pair(symbol, current))) {
                    if (tmp.intersect(tmpEndStates).isNotEmpty()) endStates.add(tmp)
                    table[Pair(symbol, current)] = tmp
                    queue.add(tmp)
                }
            }
        }
        stateCount = table.keys.map { it.second }.size
        startState = tmpStartStates
    }

    fun execute(input: List<Int>): Boolean {
        var current = startState
        input.forEach { symbol ->
            current = table[Pair(symbol, current)] ?: return false
        }
        return current in endStates
    }

    fun toDFA(): String {
        val mapper = table.keys.map { it.second }.toSet().toList().mapIndexed { index, it -> Pair(it, index) }.toMap()
        val tmp = StringBuilder()
        tmp.append("${stateCount}\n")
        tmp.append("${symbolCount}\n")
        tmp.append("${mapper[startState]}\n")
        tmp.append("${endStates.map { mapper[it] }.joinToString(separator = " ", prefix = "", postfix = "")}\n")
        table.forEach {
            tmp.append(
                "${mapper[it.key.second]} ${it.key.first} ${
                    mapper[it.value]
                }\n"
            )
        }
        return tmp.toString()
    }
}

/**
Readline path to file with FA description.
After readline with command in {"exit", "execute", "toDFA"}.
If "exit" exit program.
If "execute" read string and print FA output.
If "toDFA" transform FA to DFA and print its description.
 */
fun main() {
    val fa = FA(File(readLine() ?: "input.txt"))
    while (true) {
        val command = readLine() ?: return
        when (command) {
            "exit" -> return
            "execute" -> println(fa.execute((readLine() ?: "").split(" ").filterNot { it == "" }.map { it.toInt() }))
            "toDFA" -> println(fa.toDFA())
            else -> println("Invalid command")
        }

    }
}