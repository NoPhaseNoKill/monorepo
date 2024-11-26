import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class ExtractNpmDependenciesTest {

    @Test
    fun `should extract final unique dependencies from package lock json file`() {
        val resource = javaClass.classLoader.getResource("package-lock.json")
        requireNotNull(resource) { "Resource file not found" }
        val packageLockFile = Paths.get(resource.toURI()).toFile()

        val expectedDepsFile = javaClass.classLoader.getResource("expected-deps.txt")

        requireNotNull(expectedDepsFile) { "Expected deps file not found"}

        val expectedFile = Paths.get(expectedDepsFile.toURI()).toFile()
        val expected = expectedFile.readText()
        val result = ExtractNpmDependencies.extract(packageLockFile)

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `should show duplicate dependencies`() {

        val expectedDeps = javaClass.classLoader.getResource("expected-deps.txt")
        val expectedDupes = javaClass.classLoader.getResource("expected-duplicate-deps.txt")
        requireNotNull(expectedDeps) { "Expected deps file not found" }
        requireNotNull(expectedDupes) { "Expected dupes file not found" }

        val expected = expectedDupes.readText()

        val dependencyVersions = ExtractNpmDependencies.highlightDupes( Paths.get(expectedDeps.toURI()).toFile())
        Assertions.assertEquals(expected, dependencyVersions)

    }

    @Test
    fun `should create take existing package json and lock file, and add all unique deps to package json`() {
        val lock = javaClass.classLoader.getResource("package-lock.json")
        val packageJson = javaClass.classLoader.getResource("package.json")
        val packageNewJson = javaClass.classLoader.getResource("package-new.json")
        val packageNewExpectedJson = javaClass.classLoader.getResource("package-new-expected.json")

        requireNotNull(lock) { "Expected lockFile not found" }
        requireNotNull(packageJson) { "Expected packageJsonFile not found" }
        requireNotNull(packageNewJson) { "Expected packageNewJsonFile not found" }
        requireNotNull(packageNewExpectedJson) { "Expected packageNewExpectedJson not found" }

        val lockFile = Paths.get(lock.toURI()).toFile()
        val packageJsonFile = Paths.get(packageJson.toURI()).toFile()
        val packageNewJsonFile = Paths.get(packageNewJson.toURI()).toFile()
        val packageNewExpectedJsonFile = Paths.get(packageNewJson.toURI()).toFile()

        ExtractNpmDependencies.createPackageJsonWithNewDeps(lockFile, packageJsonFile, packageNewJsonFile)
        val result = packageNewExpectedJsonFile.readText()

        Assertions.assertEquals(result, packageNewJson.readText())
    }
}