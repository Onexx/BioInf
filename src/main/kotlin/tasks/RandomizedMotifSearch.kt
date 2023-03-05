package tasks

import Solution.writeln
import java.io.BufferedReader
import kotlin.random.Random

class RandomizedMotifSearch {
    fun solve(inputReader: BufferedReader) {
        val firstLine = inputReader.readLine().split(' ')
        val k = firstLine[0].toInt()
        val dnaStrings = ArrayList<String>()
        do {
            val line = inputReader.readLine()
            if (line != null) {
                dnaStrings.addAll(line.split(' '))
            }
        } while (line != null)


        var cnt = 0
        var bestMotifs = generateMotifs(k, dnaStrings)
        var bestScore = score(bestMotifs)
        while (cnt <= 10000) { // it solved task and I don't know any other way
            val newMotifs = generateMotifs(k, dnaStrings)
            val newScore = score(newMotifs)
            if (newScore < bestScore) {
                bestScore = newScore
                bestMotifs = newMotifs
            } else {
                cnt++
            }
        }
        for (motif in bestMotifs) {
            writeln(motif)
        }
    }

    private fun generateMotifs(k: Int, dnaStrings: ArrayList<String>): MutableList<String> {
        var bestMotifs = randomMotifs(k, dnaStrings)
        var bestScore = score(bestMotifs)
        while (true) {
            val profile = generateProfile(bestMotifs)
            val newMotifs = generateMotifsByProfile(k, profile, dnaStrings)
            val newScore = score(newMotifs)
            if (newScore < bestScore) {
                bestScore = newScore
                bestMotifs = newMotifs
            } else {
                break
            }
        }
        return bestMotifs
    }

    private fun randomMotifs(k: Int, dnaStrings: ArrayList<String>): MutableList<String> {
        val motifs = mutableListOf<String>()
        for (dnaString in dnaStrings) {
            val startPos = (0 until dnaString.length - k).random(Random(42))
            motifs.add(dnaString.substring(startPos, startPos + k))
        }
        return motifs
    }

    private fun generateMotifsByProfile(
        k: Int,
        profile: Map<Char, List<Double>>,
        dnaStrings: ArrayList<String>
    ): MutableList<String> {
        // generate main motif
        var mainMotif = ""
        for (i in 0 until k) {
            var bestEntry = 'A' to profile['A']!![i]
            for (entry in profile) {
                if (bestEntry.second < entry.value[i]) {
                    bestEntry = entry.key to entry.value[i]
                }
            }
            mainMotif += bestEntry.first
        }

        // get most probable at each idx -> motifs
        val motifs = mutableListOf<String>()
        for (dnaString in dnaStrings) {
            var bestMatch = Int.MAX_VALUE
            var bestPos = -1
            for (startPos in 0..(dnaString.length - mainMotif.length)) {
                var cnt = 0
                for (i in mainMotif.indices) {
                    if (dnaString[startPos + i] != mainMotif[i]) {
                        cnt++
                    }
                }
                if (cnt < bestMatch) {
                    bestMatch = cnt
                    bestPos = startPos
                }
                if (bestMatch == 0) break
            }
            motifs.add(dnaString.substring(bestPos, bestPos + k))
        }
        return motifs
    }


    private fun generateProfile(motifs: List<String>): Map<Char, List<Double>> {
        val profile: MutableMap<Char, MutableList<Double>> =
            mutableMapOf(
                'A' to mutableListOf(),
                'C' to mutableListOf(),
                'G' to mutableListOf(),
                'T' to mutableListOf()
            )
        for (i in 0 until motifs[0].length) {
            val counts = mutableMapOf('A' to 1.0, 'C' to 1.0, 'G' to 1.0, 'T' to 1.0)
            for (motif in motifs) {
                counts[motif[i]] = counts.getOrDefault(motif[i], 0.0) + 1.0
            }
            val total = counts.values.sum()
            for (key in profile.keys) {
                profile[key]?.add(counts.getOrDefault(key, 0.0) / total)
            }
        }
        return profile
    }


    private fun score(motifs: List<String>): Int {
        var result = ""
        var score = 0
        for (i in 0 until motifs[0].length) {
            val counters = mutableMapOf(
                'A' to 0,
                'C' to 0,
                'G' to 0,
                'T' to 0
            )
            for (motif in motifs) {
                counters[motif[i]] = counters[motif[i]]!! + 1
            }
            result += counters.entries.maxByOrNull { it.value }!!.key
            score += counters.filterKeys { it != result[i] }.values.sum()
        }
        return score
    }

}