import java.io.*

class FA(data: File) {
    private var stateCount: Int
    private var symbolCount: Int
    private var startState: Set<Int>
    private var endStates: MutableSet<Set<Int>> = mutableSetOf()
    private var table: MutableMap<Pair<Int, Set<Int>>, Set<Int>> = mutableMapOf()

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
        stateCount = table.keys.map { it.second }.toSet().size
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

    fun minimize(): String {
        // zeros step remove extra states(in init we remove its)
        val states = table.keys.map { it.second }.toMutableSet()
        states.addAll(table.values)

        // first step(add end states and another states to equals classes)
        val buffer = mutableSetOf<Set<Set<Int>>>()
        buffer.add(endStates)
        buffer.add(states.subtract(endStates))

        // second step
        while (true) {
            var newSets = listOf<Set<Set<Int>>>()
            var oldSet = setOf<Set<Int>>()
            var flag = false
            buffer.forEach { set ->
                repeat(symbolCount) { symbol ->
                    val tmp = mutableSetOf<Pair<Set<Int>, Set<Int>>>()
                    for (state in set) {
                        tmp.add(Pair(table.getValue(Pair(symbol, state)), state))
                    }
                    val new = tmp.groupBy { pair -> buffer.find { it.contains(pair.first) } }
                        .map { it.value.map { it.second }.toSet() }
                    if (new.size > 1) {
                        flag = true
                        newSets = new
                        oldSet = set
                        return@forEach
                    }
                }
            }
            if (!flag) {
                break
            } else {
                buffer.remove(oldSet)
                buffer.addAll(newSets)
            }
        }

        val mapper = buffer.mapIndexed { index, it -> Pair(it, index) }.toMap()
        val tmp = StringBuilder()
        tmp.append("${buffer.size}\n")
        tmp.append("${symbolCount}\n")
        tmp.append("${mapper[buffer.find { it.contains(startState) }]}\n")
        tmp.append(
            "${
                buffer.filter { it.any { it in endStates } }.map { mapper[it] }
                    .joinToString(separator = " ", prefix = "", postfix = "")
            }\n"
        )
        buffer.forEach { set ->
            repeat(symbolCount) { symbol ->
                tmp.append(
                    "${mapper[set]} $symbol ${
                        mapper[buffer.find {
                            it.contains(
                                table[Pair(
                                    symbol,
                                    set.first()
                                )]
                            )
                        }]
                    }\n"
                )
            }
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
            "min" -> println(fa.minimize())
            else -> println("Invalid command")
        }

    }
}