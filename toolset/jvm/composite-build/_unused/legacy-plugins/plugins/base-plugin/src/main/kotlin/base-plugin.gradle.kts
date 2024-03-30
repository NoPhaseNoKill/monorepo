plugins {
    java
}

/*
    Basic benchmarking indicates speed improves of NON-GENERIC function of anywhere between 1.5-4x.
    Considering it's 381 -> 1357 or 157976 -> 282628 nanoseconds, probably doesn't matter.
    Could be worth it if we start to use this everywhere
 */
// fun <T : Any> getLazilyEvaluatedValue(value: T): Provider<T> = provider { value }
//
// val javaVersion = getLazilyEvaluatedValue(JavaVersion.VERSION_21)
// val currentJavaVersion = getLazilyEvaluatedValue(JavaVersion.current())
// val javaLanguageVersion = getLazilyEvaluatedValue(JavaLanguageVersion.of(javaVersion.get().toString()))
// val buildJavaHome = getLazilyEvaluatedValue(System.getProperty("java.home"))
//
// gradle.beforeProject {
//     if (currentJavaVersion.get() != javaVersion.get()) {
//         throw GradleException("This build requires JDK ${javaVersion.get()}. It's currently ${buildJavaHome.get()}.")
//     }
// }
//

java {
//     /*
//         If you need to verify that the default setting: jvmTargetValidationMode = JvmTargetValidationMode.ERROR
//         works, uncomment this code AND uncomment the compiler options jvmTarget in the kotlin extension.
//         You should still see:
//
//             Execution failed for task ':list:compileKotlin'.
//             > Inconsistent JVM-target compatibility detected for tasks 'compileJava' (15) and 'compileKotlin' (21).
//     */
//     // targetCompatibility = JavaVersion.VERSION_15
//
//     /*
//         By setting this here, any kotlin tasks will also pick this up.
//
//         See: https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains-support
//     */
//     toolchain {
//         vendor = JvmVendorSpec.MICROSOFT
//         languageVersion = javaLanguageVersion
//     }

    tasks.compileJava {
        enabled = false
    }

    // tasks.compileTestJava {
    //     enabled = false
    // }
}
//
// tasks.withType<JavaCompile>().configureEach {
//     /*
//         The kotlin language spec uses this by default but this
//         protects us against any java compilation tasks not using it.
//      */
//     options.encoding = "UTF-8"
//
//     /*
//         Setting the release flag ensures the specified language level is used regardless of which compiler actually
//         performs the compilation.
//
//         For more details: https://docs.gradle.org/current/userguide/building_java_projects.html#sec:compiling_with_release
//      */
//     options.release = getLazilyEvaluatedValue(javaLanguageVersion.get().asInt())
//
//     /*
//         Runs the compiler as a separate process
//
//         Specifically:
//
//         "Gradle reuses this process within the duration the build, so the forking overhead is minimal.
//         By forking memory-intensive compilation into a separate process, we minimize garbage collection in the main
//         Gradle process. Less garbage collection means that Gradle?s infrastructure can run faster, especially when
//         you also use parallel builds."
//
//         For more details: https://docs.gradle.org/current/userguide/performance.html#optimize_the_compiler
//      */
//
//     options.isFork = false
//
//     /*
//         Ensures that incremental compilation is absolutely OFF and is not being overriden somehow.
//
//         This is due to noticed issues with caching when it was on.
//      */
//     options.isIncremental = false
//
//     /*
//         Is kept consistent with gradle.properties to ensure that single use daemon's can be made if we need them
//      */
//     options.forkOptions.jvmArgs = listOf("-Xmx2g", "-XX:MaxMetaspaceSize=384m", "-Dfile.encoding=UTF-8", "-XX:+HeapDumpOnOutOfMemoryError")
// }
//
// // See: https://docs.gradle.org/current/userguide/working_with_files.html#sec:reproducible_archives
// tasks.withType<AbstractArchiveTask>().configureEach {
//     isPreserveFileTimestamps = true
//     isReproducibleFileOrder = true
// }
//
// dependencies {
//     implementation(platform("com.nophasenokill.platform:platform"))
//     // Annotation processor does not extend implementation. For more details see: https://docs.gradle.org/current/userguide/java_plugin.html#tab:configurations
//     annotationProcessor(platform("com.nophasenokill.platform:platform"))
// }