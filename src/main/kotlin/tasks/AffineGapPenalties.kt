package tasks

import Solution.writeln
import java.io.BufferedReader

class AffineGapPenalties {
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


    fun solve(inputReader: BufferedReader) {
        val v = inputReader.readLine()
        val w = inputReader.readLine()

        val n = v.length
        val m = w.length
        val matches = Array(n + 1) { IntArray(m + 1) }
        val insertions = Array(n + 1) { IntArray(m + 1) }
        val deletions = Array(n + 1) { IntArray(m + 1) }


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
                insertions[i][j] = maxOf(
                    insertions[i - 1][j] - extensionPenalty,
                    matches[i - 1][j] - openingPenalty
                )
                deletions[i][j] = maxOf(
                    deletions[i][j - 1] - extensionPenalty,
                    matches[i][j - 1] - openingPenalty
                )
                matches[i][j] = maxOf(
                    deletions[i][j],
                    insertions[i][j],
                    matches[i - 1][j - 1] + blosum62score(v[i - 1], w[j - 1])
                )
            }
        }
        val score = matches[n][m]
        writeln(score)

        // TODO add path reconstruction
    }
}