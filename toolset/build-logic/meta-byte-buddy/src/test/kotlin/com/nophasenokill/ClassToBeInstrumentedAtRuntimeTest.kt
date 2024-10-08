package com.nophasenokill

import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.dynamic.ClassFileLocator
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers.named
import net.bytebuddy.pool.TypePool
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test


object ClassToBeInstrumentedAtRuntimeTest {

    @Test
    fun shouldShowcaseInstrumentation() {
        val annotatedClass = ClassToBeInstrumentedAtRuntime()
        val expected = "Instrumented"
        assertEquals(expected, annotatedClass.toString())
    }



    @Test
    @Disabled("Will throw warnings due to dynamically loading")
    fun `testing byte buddy`() {

        class Foo {
            fun m(): String {
                return "foo"
            }
        }


        /*
            Required to be loaded statically
         */
        ByteBuddyAgent.install()
        val foo = Foo()
        ByteBuddy()
            .redefine(Bar::class.java)
            .name(Foo::class.java.name)
            .make()
            .load(Foo::class.java.classLoader, ClassReloadingStrategy.fromInstalledAgent())
        assertEquals(foo.m(), ("bar"))
    }

    @Test
    fun `my applciation example`() {
        val typePool = TypePool.Default.ofSystemLoader()
        val bar = ByteBuddy()
            .redefine<Any>(
                typePool.describe("com.nophasenokill.Bar").resolve(),  // do not use 'Bar.class'
                ClassFileLocator.ForClassLoader.ofSystemLoader()
            )
            .defineField("qux", String::class.java) // we learn more about defining fields later
            .make()
            .load(ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.Default.INJECTION)
            .loaded

        assertNotNull(bar.getDeclaredField("qux"))
    }
}
