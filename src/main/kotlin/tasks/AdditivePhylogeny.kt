package tasks

import Solution.writeln
import Task
import java.io.BufferedReader
import kotlin.math.min

class AdditivePhylogeny : Task {
    override fun solve(inputReader: BufferedReader) {
        val n = inputReader.readLine().toInt()
        val dist = mutableListOf<MutableList<Int>>()
        for (i in 0 until n) {
            val line = inputReader.readLine().split(' ').map { it.trim() }.filter { it.isNotBlank() }.map { it.toInt() }
            dist.add(line.toMutableList())
        }
        val result = additivePhylogeny(dist, dist.size)
        result.forEachIndexed { u, list ->
            list.forEach { (v, w) ->
                writeln("$u->$v:$w")
            }
        }
    }

    private fun limb(dist: MutableList<MutableList<Int>>, n: Int): Int {
        var result = Int.MAX_VALUE

        for (i in 0 until n - 1) {
            for (j in 0 until n - 1) {
                if (i == j) continue
                result = min(result, (dist[n - 1][i] + dist[n - 1][j] - dist[j][i]) / 2)
            }
        }
        return result
    }

    private fun findLeaves(dist: MutableList<MutableList<Int>>, n: Int): Pair<Int, Int> {
        for (i in 0 until n - 1) {
            for (k in i + 1 until n - 1) {
                if (dist[i][k] == dist[i][n - 1] + dist[n - 1][k]) {
                    return Pair(i, k)
                }
            }
        }
        throw IllegalStateException("No leaves found")
    }

    private fun additivePhylogeny(
        dist: MutableList<MutableList<Int>>,
        n: Int
    ): MutableList<MutableList<Pair<Int, Int>>> {
        if (n == 2) {
            val result = MutableList(dist.size) { mutableListOf<Pair<Int, Int>>() }
            result[0].add(1 to dist[1][0])
            result[1].add(0 to dist[1][0])
            return result
        }

        val limbLength = limb(dist, n)
        for (i in 0 until n - 1) {
            dist[i][n - 1] -= limbLength
            dist[n - 1][i] = dist[i][n - 1]
        }

        val (i, k) = findLeaves(dist, n)
        val res = additivePhylogeny(dist, n - 1)


        fun insertNode(curNode: Int, prev: Int, curDist: Int): Boolean {
            if (curNode == k) {
                if (curDist == dist[i][n - 1]) {
                    res[curNode].add(n - 1 to limbLength)
                    res[n - 1].add(curNode to limbLength)
                }
                return true
            }
            for ((nextNode, nextW) in res[curNode]) {
                if (nextNode == prev) continue

                if (insertNode(nextNode, curNode, curDist + nextW)) {
                    if (curDist < dist[i][n - 1] && dist[i][n - 1] < curDist + nextW) {
                        res.add(mutableListOf())

                        res[curNode].remove(nextNode to nextW)
                        res[nextNode].remove(curNode to nextW)

                        val newNode = res.lastIndex

                        res[curNode].add(newNode to dist[i][n - 1] - curDist)
                        res[newNode].add(curNode to dist[i][n - 1] - curDist)

                        res[nextNode].add(newNode to curDist + nextW - dist[i][n - 1])
                        res[newNode].add(nextNode to curDist + nextW - dist[i][n - 1])

                        res[n - 1].add(newNode to limbLength)
                        res[newNode].add(n - 1 to limbLength)
                    }
                    return true
                }
            }
            return false
        }

        insertNode(i, -1, 0)
        return res
    }
}
