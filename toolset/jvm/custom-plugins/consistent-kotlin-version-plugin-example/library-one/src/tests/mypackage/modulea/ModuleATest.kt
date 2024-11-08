package mypackage.modulea

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ModuleATest {
    @Test
    fun `should return value specific to java file`() {
        val result = ModuleA().doWork()

        Assertions.assertEquals("/b", result)
    }
}
