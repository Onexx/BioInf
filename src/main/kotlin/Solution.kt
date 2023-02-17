import Util.getFileReader
import java.io.BufferedReader

class Solution {
    private val output = ArrayList<String>()

    fun run(taskNumber: String, filepath: String): List<String> {
        val inputReader = getFileReader(filepath)
        when (taskNumber){
            "ba2b" -> solveFirstTask(inputReader)
            else -> throw IllegalStateException("Task with number [$taskNumber] not found")
        }

        return output
    }

    private fun solveFirstTask(inputReader: BufferedReader) {
        val k = inputReader.readLine().toInt()
        val dnaStrings = ArrayList<String>()
        do {
            val line = inputReader.readLine()
            if (line != null) {
                dnaStrings.addAll(line.split(' '))
            }
        } while (line != null)

        generateFragments(k, "")
        var result = fragments.first()
        var minDistance = Int.MAX_VALUE
        fragments.forEach { fragment ->
            val curDistance = distance(fragment, dnaStrings)
            if (curDistance < minDistance) {
                minDistance = curDistance
                result = fragment
            }
        }
        write(result)
    }

    private fun distance(fragment: String, dnaStrings: ArrayList<String>): Int {
        var result = 0
        for (dnaString in dnaStrings) {
            var bestMatch = Int.MAX_VALUE
            for (startPos in 0..(dnaString.length - fragment.length)) {
                var cnt = 0
                for (i in fragment.indices) {
                    if (dnaString[startPos + i] != fragment[i]) {
                        cnt++
                    }
                }
                if (cnt < bestMatch) {
                    bestMatch = cnt
                }
                if (bestMatch == 0) break
            }
            result += bestMatch
        }
        return result
    }

    private val nucleotides = listOf('A', 'C', 'G', 'T')
    private val fragments = ArrayList<String>()

    private fun generateFragments(k: Int, prefix: String) {
        if (prefix.length == k) {
            fragments.add(prefix)
            return
        }
        for (nucleotide in nucleotides) {
            generateFragments(k, prefix + nucleotide)
        }
    }

    private fun write(text: String) {
        if (output.isEmpty()) output.add("")
        output[output.lastIndex] = output.last() + text
    }

    private fun writeln(text: String) {
        output.add(text)
    }
}