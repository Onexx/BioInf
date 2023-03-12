import Util.getFileReader
import tasks.MedianString
import tasks.PairedComposition
import tasks.RandomizedMotifSearch

object Solution {
    private val output = ArrayList<String>()

    fun run(taskCode: String, filepath: String): List<String> {
        val inputReader = getFileReader(filepath)
        when (taskCode) {
            "ba2b" -> MedianString().solve(inputReader)
            "ba2f" -> RandomizedMotifSearch().solve(inputReader)
            "ba3j" -> PairedComposition().solve(inputReader)
            else -> throw IllegalStateException("Task with code [$taskCode] not found")
        }

        return output
    }

    fun write(text: String) {
        if (output.isEmpty()) output.add("")
        output[output.lastIndex] = output.last() + text
    }

    fun writeln(text: String) {
        output.add(text)
    }

    fun clearOutput() {
        output.clear()
    }
}