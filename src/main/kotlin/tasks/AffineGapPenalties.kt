package tasks

import Solution.write
import Solution.writeln
import Task
import tasks.AffineGapPenalties.SourceType.*
import java.io.BufferedReader

class AffineGapPenalties : Task {
    enum class SourceType {
        INSERTIONS, DELETIONS, MATCHES
    }

    private val blosum62alphabet =
        charArrayOf('A', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'Y')

    private val blosum62matrix = listOf(
        listOf(4, 0, -2, -1, -2, 0, -2, -1, -1, -1, -1, -2, -1, -1, -1, 1, 0, 0, -3, -2),
        listOf(0, 9, -3, -4, -2, -3, -3, -1, -3, -1, -1, -3, -3, -3, -3, -1, -1, -1, -2, -2),
        listOf(-2, -3, 6, 2, -3, -1, -1, -3, -1, -4, -3, 1, -1, 0, -2, 0, -1, -3, -4, -3),
        listOf(-1, -4, 2, 5, -3, -2, 0, -3, 1, -3, -2, 0, -1, 2, 0, 0, -1, -2, -3, -2),
        listOf(-2, -2, -3, -3, 6, -3, -1, 0, -3, 0, 0, -3, -4, -3, -3, -2, -2, -1, 1, 3),
        listOf(0, -3, -1, -2, -3, 6, -2, -4, -2, -4, -3, 0, -2, -2, -2, 0, -2, -3, -2, -3),
        listOf(-2, -3, -1, 0, -1, -2, 8, -3, -1, -3, -2, 1, -2, 0, 0, -1, -2, -3, -2, 2),
        listOf(-1, -1, -3, -3, 0, -4, -3, 4, -3, 2, 1, -3, -3, -3, -3, -2, -1, 3, -3, -1),
        listOf(-1, -3, -1, 1, -3, -2, -1, -3, 5, -2, -1, 0, -1, 1, 2, 0, -1, -2, -3, -2),
        listOf(-1, -1, -4, -3, 0, -4, -3, 2, -2, 4, 2, -3, -3, -2, -2, -2, -1, 1, -2, -1),
        listOf(-1, -1, -3, -2, 0, -3, -2, 1, -1, 2, 5, -2, -2, 0, -1, -1, -1, 1, -1, -1),
        listOf(-2, -3, 1, 0, -3, 0, 1, -3, 0, -3, -2, 6, -2, 0, 0, 1, 0, -3, -4, -2),
        listOf(-1, -3, -1, -1, -4, -2, -2, -3, -1, -3, -2, -2, 7, -1, -2, -1, -1, -2, -4, -3),
        listOf(-1, -3, 0, 2, -3, -2, 0, -3, 1, -2, 0, 0, -1, 5, 1, 0, -1, -2, -2, -1),
        listOf(-1, -3, -2, 0, -3, -2, 0, -3, 2, -2, -1, 0, -2, 1, 5, -1, -1, -3, -3, -2),
        listOf(1, -1, 0, 0, -2, 0, -1, -2, 0, -2, -1, 1, -1, 0, -1, 4, 1, -2, -3, -2),
        listOf(0, -1, -1, -1, -2, -2, -2, -1, -1, -1, -1, 0, -1, -1, -1, 1, 5, 0, -2, -2),
        listOf(0, -1, -3, -2, -1, -3, -3, 3, -2, 1, 1, -3, -2, -2, -3, -2, 0, 4, -3, -1),
        listOf(-3, -2, -4, -3, 1, -2, -2, -3, -3, -2, -1, -4, -4, -2, -3, -3, -2, -3, 11, 2),
        listOf(-2, -2, -3, -2, 3, -3, 2, -1, -2, -1, -1, -2, -3, -1, -2, -2, -2, -1, 2, 7)
    )
    private val openingPenalty = 11
    private val extensionPenalty = 1

    fun blosum62score(x: Char, y: Char): Int {
        return blosum62matrix[blosum62alphabet.indexOf(x)][blosum62alphabet.indexOf(y)]
    }


    override fun solve(inputReader: BufferedReader) {
        val v = inputReader.readLine()
        val w = inputReader.readLine()

        val n = v.length
        val m = w.length
        val matches = Array(n + 1) { IntArray(m + 1) }
        val insertions = Array(n + 1) { IntArray(m + 1) }
        val deletions = Array(n + 1) { IntArray(m + 1) }

        val par = Array(3) { Array(n + 1) { Array(m + 1) { MATCHES } } }

        for (i in 1..n) {
            matches[i][0] = -openingPenalty - (i - 1) * extensionPenalty
            insertions[i][0] = -openingPenalty - (i - 1) * extensionPenalty
            deletions[i][0] = -openingPenalty - (i - 1) * extensionPenalty
        }
        for (i in 1..m) {
            matches[0][i] = -openingPenalty - (i - 1) * extensionPenalty
            insertions[0][i] = -openingPenalty - (i - 1) * extensionPenalty
            deletions[0][i] = -openingPenalty - (i - 1) * extensionPenalty
        }

        for (i in 1..n) {
            for (j in 1..m) {
                fun maxWithPar(
                    type: Int,
                    insertionsVal: Int = Int.MIN_VALUE,
                    deletionsVal: Int = Int.MIN_VALUE,
                    matchesVal: Int = Int.MIN_VALUE
                ): Int {
                    val maxVal = maxOf(insertionsVal, deletionsVal, matchesVal)
                    par[type][i][j] = when (maxVal) {
                        insertionsVal -> INSERTIONS
                        deletionsVal -> DELETIONS
                        else -> MATCHES
                    }
                    return maxVal
                }

                insertions[i][j] = maxWithPar(
                    0,
                    insertionsVal = insertions[i - 1][j] - extensionPenalty,
                    matchesVal = matches[i - 1][j] - openingPenalty
                )
                deletions[i][j] = maxWithPar(
                    1,
                    deletionsVal = deletions[i][j - 1] - extensionPenalty,
                    matchesVal = matches[i][j - 1] - openingPenalty
                )
                matches[i][j] = maxWithPar(
                    2,
                    deletionsVal = deletions[i][j],
                    insertionsVal = insertions[i][j],
                    matchesVal = matches[i - 1][j - 1] + blosum62score(v[i - 1], w[j - 1])
                )
            }
        }

        writeln(matches[n][m])

        var type = 2 // matches
        var x = n
        var y = m
        val result = mutableListOf<SourceType>()
        while (x != 0 && y != 0) {
            val sourceType = par[type][x][y]
            when (type) {
                0 -> {
                    x--
                    if (sourceType == MATCHES) {
                        type = 2 // matches
                    } else {
                        result.add(sourceType)
                    }
                }

                1 -> {
                    y--
                    if (sourceType == MATCHES) {
                        type = 2 // matches
                    } else {
                        result.add(sourceType)
                    }
                }

                2 -> {
                    result.add(sourceType)
                    when (sourceType) {
                        INSERTIONS -> type = 0
                        DELETIONS -> type = 1
                        MATCHES -> {
                            x--
                            y--
                        }
                    }
                }
            }
        }
        result.reverse()
        // v
        var ptr = 0
        result.forEach {
            when (it) {
                INSERTIONS -> write(v[ptr++])
                DELETIONS -> write("-")
                MATCHES -> write(v[ptr++])
            }
        }
        writeln("")
        // w
        ptr = 0
        result.forEach {
            when (it) {
                INSERTIONS -> write("-")
                DELETIONS -> write(w[ptr++])
                MATCHES -> write(w[ptr++])
            }
        }
    }
}