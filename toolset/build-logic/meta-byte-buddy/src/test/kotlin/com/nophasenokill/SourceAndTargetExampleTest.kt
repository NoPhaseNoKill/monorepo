package com.nophasenokill

import net.bytebuddy.ByteBuddy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers.named
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class SourceAndTargetExampleTest {

    @Test
    fun `should showcase the from and to class (changes from class X to class Y)`() {


        val helloWorld = ByteBuddy()
            .subclass(Source::class.java)
            .method(named("hello")).intercept(MethodDelegation.to(Target::class.java))
            .make()
            .load(javaClass.classLoader)
            .loaded
            .getDeclaredConstructor()
            .newInstance()
            .hello("World")

        assertNotNull(helloWorld)
    }

}

class Source {
    fun hello(name: String?): String? {
        return null
    }
}

object Target {
    @JvmStatic
    fun hello(name: String): String {
        return "Hello $name!"
    }
}


