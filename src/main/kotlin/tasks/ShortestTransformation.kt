package tasks

import Solution.write
import Solution.writeln
import java.io.BufferedReader

typealias Genome = List<Int>
typealias Edge = Pair<Int, Int>

class ShortestTransformation {
    fun solve(inputReader: BufferedReader) {
        val firstLine = inputReader.readLine()
        val firstGenome = firstLine.split("(", " ", ")").filter { it.isNotBlank() }.map { it.toInt() }
        writeln(firstLine)
        val secondGenome = inputReader.readLine().split("(", " ", ")").filter { it.isNotBlank() }.map { it.toInt() }
        twoBreakSorting(firstGenome, secondGenome)
    }

    private fun twoBreakSorting(genome1: Genome, genome2: Genome) {
        val greenEdges = genomeToEdges(genome1).toMutableSet()
        val redEdges = genomeToEdges(genome2).toMutableSet()
        var cycles = findNonTrivialCycles(redEdges, greenEdges)
        while (cycles.isNotEmpty()) {
            val cycle = cycles.first()
            twoBreak(greenEdges, cycle)
            edgesToGenome(greenEdges)
            cycles = findNonTrivialCycles(redEdges, greenEdges)
        }
    }

    private fun twoBreak(edges: MutableSet<Edge>, cycle: List<Int>) {
        edges.remove(Edge(cycle[0], cycle[1]))
        edges.remove(Edge(cycle[1], cycle[0]))
        edges.remove(Edge(cycle[2], cycle[3]))
        edges.remove(Edge(cycle[3], cycle[2]))
        edges.add(Edge(cycle[1], cycle[2]))
        edges.add(Edge(cycle[0], cycle[3]))
    }

    private fun genomeToEdges(genome: Genome): Set<Edge> {
        val result = mutableSetOf<Edge>()
        for (i in 0 until genome.size - 1) {
            result.add(Edge(-genome[i], genome[i + 1]))
        }
        result.add(Edge(-genome.last(), genome.first()))
        return result
    }

    private fun edgesToGenome(greenEdges: Set<Edge>) {
        val graph = mutableMapOf<Int, MutableList<Int>>()
        (1..greenEdges.size).forEach { idx ->
            graph.putIfAbsent(idx, mutableListOf())
            graph[idx]!!.add(-idx)
            graph.putIfAbsent(-idx, mutableListOf())
            graph[-idx]!!.add(idx)
        }
        greenEdges.forEach { (u, v) ->
            graph.putIfAbsent(u, mutableListOf())
            graph[u]!!.add(v)
            graph.putIfAbsent(v, mutableListOf())
            graph[v]!!.add(u)
        }
        val visited = mutableSetOf<Int>()

        for ((u, v) in greenEdges) {
            if (visited.contains(u)) continue
            visited.add(u)
            var nv = v
            val cycle = mutableListOf(u)
            while (nv != u) {
                visited.add(nv)
                cycle.add(nv)
                nv = if (graph[nv]!![0] != cycle[cycle.size - 2]) graph[nv]!![0] else graph[nv]!![1]
            }
            write("(")
            var space = false
            for (i in 0 until cycle.size / 2) {
                if (space) write(' ')
                space = true
                if (-cycle[i * 2] > 0) write('+')
                write("${-cycle[i * 2]}")
            }
            write(")")
        }
        writeln("")
    }

    private fun findNonTrivialCycles(redEdges: Set<Edge>, greenEdges: Set<Edge>): List<List<Int>> {
        val graph = mutableMapOf<Int, MutableList<Int>>()
        redEdges.forEach { (u, v) ->
            graph.putIfAbsent(u, mutableListOf())
            graph[u]!!.add(v)
            graph.putIfAbsent(v, mutableListOf())
            graph[v]!!.add(u)
        }
        greenEdges.forEach { (u, v) ->
            graph.putIfAbsent(u, mutableListOf())
            graph[u]!!.add(v)
            graph.putIfAbsent(v, mutableListOf())
            graph[v]!!.add(u)
        }
        val visited = mutableSetOf<Int>()
        val cycles = mutableListOf<List<Int>>()

        for ((u, v) in greenEdges) {
            if (visited.contains(u)) continue
            visited.add(u)
            var nv = v
            val cycle = mutableListOf(u)
            while (nv != u) {
                visited.add(nv)
                cycle.add(nv)
                nv = if (graph[nv]!![0] != cycle[cycle.size - 2]) graph[nv]!![0] else graph[nv]!![1]
            }
            if (cycle.size > 2) cycles.add(cycle)
        }
        return cycles
    }
}