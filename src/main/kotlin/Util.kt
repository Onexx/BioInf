import java.io.BufferedReader
import java.io.InputStream

object Util {

    fun getFileReader(filepath: String): BufferedReader {
        val resourceStream: InputStream? = this.javaClass.classLoader.getResourceAsStream(filepath)
        if (resourceStream == null) {
            System.err.println("Couldn't load input on path [$filepath]: resourceStream is null")
        }
        return resourceStream!!.bufferedReader(Charsets.UTF_8)
    }
}