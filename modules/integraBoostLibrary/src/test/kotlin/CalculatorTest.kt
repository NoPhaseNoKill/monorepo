import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CalculatorTest {

    @Test
    fun `should add two numbers`() {
        val result = Calculator.add(2, 3)
        assertEquals(5, result)
    }
}