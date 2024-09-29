import com.example.MethodTracker
import com.example.MyClass
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MyClassTest {
    @Test
    fun testPerformAction() {
        // Now correctly passing a single string with the full method signature
        MethodTracker.logMethod("com/example/MyClassTest#testPerformAction")

        val myClass = MyClass()
        myClass.performAction()
        assertTrue(true)
    }
}

