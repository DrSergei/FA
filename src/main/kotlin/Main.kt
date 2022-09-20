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

    override fun toString(): String {
        val tmp = StringBuilder()
        tmp.append("${stateCount}\n")
        tmp.append("${symbolCount}\n")
        tmp.append("${startState}\n")
        tmp.append("${endStates}\n")
        table.forEach {
            tmp.append("${it.key.second} ${it.key.first} ${it.value}\n")
        }
        return tmp.toString()
    }
}

fun main() {
    val fa = FA(File(readLine() ?: "input.txt"))
    println(fa.execute((readLine() ?: "").split(" ").filterNot { it == "" }.map { it.toInt() }))
}