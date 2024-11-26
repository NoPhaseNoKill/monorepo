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

    @Test
    fun `should calc relative path correctly`() {
        val settingsFilePathOne ="/some/absolute/root/settings/path"
        val rootPathOne = Paths.get(settingsFilePathOne)
        val projectPathOne = Paths.get("/some/absolute/root/settings/path/with/project/path")
        val relativeProjectPathOne = rootPathOne.relativize(projectPathOne)

        val settingsFilePathTwo ="/some/settings/path"
        val rootPathTwo = Paths.get(settingsFilePathTwo)
        val projectPathTwo = Paths.get("/some/settings/path/with/project/path")
        val relativeProjectPathTwo = rootPathTwo.relativize(projectPathTwo)

        Assertions.assertEquals(relativeProjectPathOne, relativeProjectPathTwo)
    }
}