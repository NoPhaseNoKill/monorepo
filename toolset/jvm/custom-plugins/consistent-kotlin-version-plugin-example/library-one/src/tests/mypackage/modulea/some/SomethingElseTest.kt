package mypackage.modulea.some

import mypackage.modulea.some.nested.dir.SomethingElse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SomethingElseTest {

    @Test
    fun `should return values indicating that this file uses both java and kotlin compiled classes`() {
        val javaAndKotlinResult = SomethingElse.doSomethingElse()
        val expected = "/bBob smith"
        Assertions.assertEquals(expected, javaAndKotlinResult)
    }
}
