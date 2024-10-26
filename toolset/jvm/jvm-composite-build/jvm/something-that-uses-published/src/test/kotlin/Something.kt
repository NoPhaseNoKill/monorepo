import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class UsesRootBuildFileTest {

    @Test
    fun doSomething() {
        val result = UsesRootBuildFile.useFile()
        // Should pass the assert which happens from the library, and then return an updated result
        Assertions.assertEquals("Ohai2", result)
    }
}
