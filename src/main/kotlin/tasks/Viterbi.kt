package tasks

import Solution.write
import Task
import java.io.BufferedReader

class Viterbi : Task {
    override fun solve(inputReader: BufferedReader) {
        val str = inputReader.readLine()
        inputReader.readLine()
        val alphabet = inputReader.readLine().split("\\s+".toRegex()).filter { it.isNotBlank() }
        inputReader.readLine()
        val states = inputReader.readLine().split("\\s+".toRegex()).filter { it.isNotBlank() }
        inputReader.readLine()
        val transitions = readTable(inputReader, states.size)
        inputReader.readLine()
        val emission = readTable(inputReader, states.size)

        write(viterbi(str, alphabet, states, transitions, emission))
    }

    private fun viterbi(
        str: String,
        alphabet: List<String>,
        states: List<String>,
        transitions: List<List<Double>>,
        emissions: List<List<Double>>
    ): String {
        val probs = Array(states.size) { DoubleArray(str.length) }
        val path = Array(states.size) { IntArray(str.length) }
        val observations = str.map { alphabet.indexOf(it.toString()) }

        for (i in states.indices) {
            probs[i][0] = emissions[i][observations[0]]
            path[i][0] = i
        }

        for (j in 1 until str.length) {
            for (i in states.indices) {
                val emission = emissions[i][observations[j]]

                var maxProb = -Double.MAX_VALUE
                var maxState = -1
                for (k in probs.indices) {
                    val prob = probs[k][j - 1] * transitions[k][i] * emission
                    if (prob > maxProb) {
                        maxProb = prob
                        maxState = k
                    }
                }
                probs[i][j] = maxProb
                path[i][j - 1] = maxState
            }
        }

        var maxProb = -Double.MAX_VALUE
        var lastState = -1
        for (i in states.indices) {
            if (probs[i][str.length - 1] > maxProb) {
                maxProb = probs[i][str.length - 1]
                lastState = i
            }
        }

        val pi = mutableListOf(lastState)
        for (j in str.length - 2 downTo 0) {
            pi.add(0, path[pi[0]][j])
        }

        return pi.joinToString("") { states[it] }
    }


    private fun readTable(inputReader: BufferedReader, size: Int): List<List<Double>> {
        val result = mutableListOf<List<Double>>()
        inputReader.readLine()
        repeat(size) {
            val q = inputReader.readLine()
            val line = q
                .split("\\s+".toRegex())
                .filter { it.isNotBlank() }
                .drop(1)
                .map { it.toDouble() }
            result.add(line)
        }
        return result
    }
}