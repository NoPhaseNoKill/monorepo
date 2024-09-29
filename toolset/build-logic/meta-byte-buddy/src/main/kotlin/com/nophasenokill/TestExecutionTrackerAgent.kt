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
            .type(isAnnotatedWith(InstrumentedAnnotation::class.java))
            .transform { builder, typeDescription, classloader, javaModule, protectionDomain ->
                builder.method(named<NamedElement>("toString"))
                    .intercept(FixedValue.value("Instrumented"))
            }
            .installOn(instrumentation)
    }
}

/*
    Example of how to see some classes loaded.

    Note: This outputs duplicates and is not properly refined, hence why I've commented it out
 */

// object TestExecutionTrackerAgent {
//     private val transformedClasses = mutableSetOf<String>()
//
//     @JvmStatic
//     fun premain(arguments: String?, instrumentation: Instrumentation) {
//         AgentBuilder.Default()
//             // Apply transformation to non-Java/Kotlin classes
//             .type(
//                 not(
//                     nameStartsWith<NamedElement>("java.")
//                         .or(nameStartsWith("javax."))
//                         .or(nameStartsWith("kotlin."))
//                         .or(nameStartsWith("sun."))
//                         .or(nameStartsWith("jdk."))
//                         .or(nameStartsWith("org.gradle"))
//                         .or(nameStartsWith("org.opentest4j"))
//                 )
//             )
//             .transform { builder, typeDescription, classLoader, javaModule, protectionDomain ->
//                 builder.visit(Advice.to(LoggingAdvice::class.java).on(any()))
//             }
//             .with(ClassBytecodeLoggingListener("/tmp/instrumentation"))
//             .installOn(instrumentation)
//     }
// }
//
// object LoggingAdvice {
//     @Advice.OnMethodEnter
//     @JvmStatic
//     fun onEnter(@Advice.Origin clazz: Class<*>) {
//         if (clazz.name.contains("nophasenokill")) {
//             println("Class Loaded: ${clazz.name}")
//         }
//     }
// }
//
// // Listener to capture and log bytecode
// class ClassBytecodeLoggingListener(private val outputDir: String) : AgentBuilder.Listener {
//     private val processedClasses = mutableSetOf<String>() // Track processed classes
//
//     override fun onDiscovery(typeName: String, classLoader: ClassLoader?, module: JavaModule?, loaded: Boolean) {
//
//     }
//
//     override fun onTransformation(
//         typeDescription: TypeDescription,
//         @MaybeNull classLoader: ClassLoader?,
//         @MaybeNull javaModule: JavaModule?,
//         loaded: Boolean,
//         dynamicType: DynamicType
//     ) {
//         // Avoid processing the same class multiple times
//         if (typeDescription.name.contains("nophasenokill") && processedClasses.add(typeDescription.name)) {
//             val className = typeDescription.name.replace('.', '/')
//             val filePath = Paths.get(outputDir, "$className.class")
//
//             // Ensure output directory exists
//             Files.createDirectories(filePath.parent)
//
//             // Write the bytecode to a file
//             Files.write(filePath, dynamicType.bytes, StandardOpenOption.CREATE)
//             println("Saved instrumented bytecode for class: ${typeDescription.name} at $filePath")
//
//             println("Transformed class: ${typeDescription.name}")
//             val bytecode = dynamicType.bytes
//             println("Bytecode (Hex): ${byteArrayToHex(bytecode)}")
//         }
//     }
//
//     override fun onIgnored(typeDescription: TypeDescription, classLoader: ClassLoader?, javaModule: JavaModule?, loaded: Boolean) {
//         // Ignored classes are not processed
//     }
//
//     override fun onError(typeName: String, classLoader: ClassLoader?, javaModule: JavaModule?, loaded: Boolean, throwable: Throwable) {
//         // Handle transformation errors
//     }
//
//     override fun onComplete(typeName: String, classLoader: ClassLoader?, javaModule: JavaModule?, loaded: Boolean) {
//         // Transformation complete
//     }
//
//     private fun byteArrayToHex(bytes: ByteArray): String {
//         return bytes.joinToString("") { String.format("%02x", it) }
//     }
// }
