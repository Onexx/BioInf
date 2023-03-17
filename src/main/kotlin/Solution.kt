import Util.getFileReader
import tasks.AffineGapPenalties
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
            "ba5j" -> AffineGapPenalties().solve(inputReader)
            else -> throw IllegalStateException("Task with code [$taskCode] not found")
        }

        return output
    }

    fun write(text: Any) {
        if (output.isEmpty()) output.add("")
        output[output.lastIndex] = output.last() + text.toString()
    }

    fun writeln(text: Any) {
        if (output.isEmpty()) output.add("")
        output[output.lastIndex] = output.last() + text.toString()
        output.add("")
    }

    fun clearOutput() {
        output.clear()
    }
}