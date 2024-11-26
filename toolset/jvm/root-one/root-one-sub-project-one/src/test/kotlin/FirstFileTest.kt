import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths

class FirstFileTest {

    @Test
    fun `check classloader name`() {
        val result = FirstFile().print()
        val buildDir = File(result).resolve("build/custom-task/print-relative-project-dir-path/output-file.txt")
        val buildDirPath = Paths.get(buildDir.path).toFile().path
        val textFromTaskOutput = File(buildDirPath).readText()
        Assertions.assertTrue(result.endsWith(textFromTaskOutput))
    }
}