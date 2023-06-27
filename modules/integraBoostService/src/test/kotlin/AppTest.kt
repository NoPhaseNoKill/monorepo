import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AppTest {

    @Test
    fun `should greet with hello world`() {
        val app = App()
        val greeting = app.greeting

        assertEquals(greeting, "Hello World!")
    }
}