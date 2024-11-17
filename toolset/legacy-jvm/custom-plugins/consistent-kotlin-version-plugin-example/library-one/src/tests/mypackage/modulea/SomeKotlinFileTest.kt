package mypackage.modulea

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SomeKotlinFileTest {

    @Test
    fun `should return bob smith`() {
        val result = SomeKotlinFile.doSomething()
        Assertions.assertEquals("Bob smith", result)
    }
}
