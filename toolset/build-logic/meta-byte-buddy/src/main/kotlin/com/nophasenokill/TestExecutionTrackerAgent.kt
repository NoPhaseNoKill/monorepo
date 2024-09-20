package com.nophasenokill
import net.bytebuddy.agent.builder.AgentBuilder
import net.bytebuddy.description.NamedElement
import net.bytebuddy.implementation.FixedValue
import net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith
import net.bytebuddy.matcher.ElementMatchers.named
import java.lang.instrument.Instrumentation

object TestExecutionTrackerAgent {
    @JvmStatic
    fun premain(arguments: String?, instrumentation: Instrumentation) {
        AgentBuilder.Default()
            .type(isAnnotatedWith(InstrumentedAnnotation::class.java))  // Filter for classes annotated with @ToString
            .transform { builder, typeDescription, classloader, javaModule, protectionDomain ->
                builder.method(named<NamedElement>("toString"))
                    .intercept(FixedValue.value("Instrumented"))
            }
            .installOn(instrumentation)
    }
}
