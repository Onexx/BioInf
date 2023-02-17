import org.junit.Test
import java.io.File

class Test {
    companion object {
        private const val ANSI_RESET = "\u001B[0m"
        private const val ANSI_RED = "\u001B[31m"
        private const val ANSI_GREEN = "\u001B[32m"
    }

    private var failedTestsCounter = 0
    private var passedTestsCounter = 0

    @Test
    fun test() {
        var idx = 1
        for (t in 0..10) {
            val inputPath = "inputs/input_$idx.txt"
            val outputPath = "outputs/output_$idx.txt"
            val inputFile = File("src/test/resources/$inputPath")
            val outputFile = File("src/test/resources/$outputPath")
            if (inputFile.exists() && outputFile.exists()) {
                val result = Solution().run(inputPath)
                val expected = this.javaClass.classLoader.getResourceAsStream(outputPath)!!
                    .bufferedReader(Charsets.UTF_8).useLines { it.toList() }

                if (result == expected) {
                    passedTestsCounter++
                    println("${ANSI_GREEN}Passed test $idx $ANSI_RESET")
                } else {
                    failedTestsCounter++
                    val notFound = (expected - result.toSet())
                    val extra = (result - expected.toSet())

                    println("${ANSI_RED}Failed test $idx")
                    println("========================")
                    if (notFound.isNotEmpty()) println("Couldn't find strings: \n${notFound.joinToString("\n")}")
                    if (extra.isNotEmpty()) println("Found extra strings: \n${extra.joinToString("\n")}")
                    println("========================")
                    println(ANSI_RESET)
                }
                idx++
            } else {
                break
            }
        }
        println("########################")
        if (failedTestsCounter == 0) {
            println(ANSI_GREEN)
        } else {
            println(ANSI_RED)
        }
        println("Passed $passedTestsCounter/${passedTestsCounter + failedTestsCounter} tests")
        println(ANSI_RESET)
        println("########################")
    }
}