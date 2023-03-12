package tasks

import Solution.writeln
import java.io.BufferedReader

class PairedComposition {
    fun solve(inputReader: BufferedReader) {
        val firstLine = inputReader.readLine().split(' ')
        val k = firstLine[0].toInt()
        val d = firstLine[1].toInt()

        val graph = mutableMapOf<Pair<String, String>, Pair<String, String>?>()
        val isStart = mutableMapOf<Pair<String, String>, Boolean?>()
        do {
            val line = inputReader.readLine()
            if (line != null) {
                val kdmer = line.split('|')
                val cur = kdmer[0] to kdmer[1]
                var next: Pair<String, String>? = null
                for (vertex in graph) {
                    if (vertex.key.getSuffix() == cur.getPrefix()) {
                        // vertex -> cur
                        vertex.setValue(cur)
                        isStart[cur] = false
                    }
                    if (vertex.key.getPrefix() == cur.getSuffix()) {
                        // cur -> vertex
                        next = vertex.key
                        isStart[vertex.key] = false
                    }
                }
                graph[cur] = next
                isStart.putIfAbsent(cur, true)
            }
        } while (line != null)
        val result = StringBuilder("?".repeat(k * 2 + d + graph.size - 1))
        val start = isStart.firstNotNullOf { item -> item.takeIf { it.value == true } }

        start.key.first.forEachIndexed { idx, c -> result[idx] = c }
        start.key.second.forEachIndexed { idx, c -> result[k + d + idx] = c }

        var cur: Pair<String, String>? = graph[start.key]
        var idx = k
        while (cur != null) {
            result[idx] = cur.first[k - 1]
            result[k + d + idx] = cur.second[k - 1]
            idx++

            cur = graph[cur]
        }
        writeln(result.toString())
    }

    private fun Pair<String, String>.getPrefix(): Pair<String, String> {
        return first.substring(0, first.length - 1) to second.substring(0, second.length - 1)
    }

    private fun Pair<String, String>.getSuffix(): Pair<String, String> {
        return first.substring(1) to second.substring(1)
    }
}
