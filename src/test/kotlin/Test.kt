import org.junit.Test
import java.io.File
import kotlin.test.fail

class Test {
    companion object {
        private const val ANSI_RESET = "\u001B[0m"
        private const val ANSI_RED = "\u001B[31m"
        private const val ANSI_GREEN = "\u001B[32m"
    }

    private var failedTestsCounter = 0
    private var passedTestsCounter = 0

    @Test
    fun medianStringTest() {
        runTests(taskCode = "ba2b")
    }

    @Test
    fun randomizedMotifSearchTest() {
        runTests(taskCode = "ba2f")
    }

    @Test
    fun pairedCompositionTest() {
        runTests(taskCode = "ba3j")
    }

    @Test
    fun affineGapPenaltiesTest() {
        runTests(taskCode = "ba5j")
    }

    @Test
    fun shortestTransformationTest() {
        runTests(taskCode = "ba6d")
    }

    @Test
    fun additivePhylogenyTest() {
        runTests(taskCode = "ba7c")
    }

    @Test
    fun longestRepeatTest() {
        runTests(taskCode = "ba9d")
    }

    @Test
    fun viterbiTest() {
        runTests(taskCode = "ba10c")
    }

    private fun runTests(taskCode: String) {
        var idx = 1
        while (true) {
            val inputPath = "$taskCode/inputs/input_$idx.txt"
            val outputPath = "$taskCode/outputs/output_$idx.txt"
            val inputFile = File("src/test/resources/$inputPath")
            val outputFile = File("src/test/resources/$outputPath")
            if (inputFile.exists() && outputFile.exists()) {
                val result = Solution.run(taskCode, inputPath).filter { it.isNotBlank() }
                val expected = this.javaClass.classLoader.getResourceAsStream(outputPath)!!
                    .bufferedReader(Charsets.UTF_8).useLines { it.toList() }.filter { it.isNotBlank() }

                if (result == expected) {
                    passedTestsCounter++
                    println("${ANSI_GREEN}Passed $taskCode test $idx $ANSI_RESET")
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
                Solution.clearOutput()
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
        if (failedTestsCounter != 0) fail()
    }
}