package tasks

import Solution.write
import Task
import java.io.BufferedReader

class LongestRepeat : Task {

    private fun String.countSubstring(substring: String): Int {
        var index = 0
        var count = 0

        while (true) {
            index = indexOf(substring, index)
            if (index != -1) {
                count++
                index++
            } else {
                return count
            }
        }
    }

    override fun solve(inputReader: BufferedReader) {
        val s = inputReader.readLine()
        for (sz in s.length - 1 downTo 1) {
            val result = s.windowed(sz).find { s.countSubstring(it) > 1 }
            if (!result.isNullOrBlank()) {
                write(result)
                return
            }
        }

        write("nan")
    }
}