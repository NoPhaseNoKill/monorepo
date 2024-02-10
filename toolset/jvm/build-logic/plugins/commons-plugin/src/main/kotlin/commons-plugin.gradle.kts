import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin.Companion.isIncludeCompileClasspath
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("base")
    id("java")
    id("org.jetbrains.kotlin.jvm")
}

/*
    Basic benchmarking indicates speed improves of NON-GENERIC function of anywhere between 1.5-4x.
    Considering it's 381 -> 1357 or 157976 -> 282628 nanoseconds, probably doesn't matter.
    Could be worth it if we start to use this everywhere
 */
fun <T : Any> getLazilyEvaluatedValue(value: T): Provider<T> = provider { value }

val javaVersion = getLazilyEvaluatedValue(JavaVersion.VERSION_21)
val currentJavaVersion = getLazilyEvaluatedValue(JavaVersion.current())
val javaLanguageVersion = getLazilyEvaluatedValue(JavaLanguageVersion.of(javaVersion.get().toString()))
val buildJavaHome = getLazilyEvaluatedValue(System.getProperty("java.home"))

gradle.beforeProject {
    if (currentJavaVersion.get() != javaVersion.get()) {
        throw GradleException("This build requires JDK ${javaVersion.get()}. It's currently ${buildJavaHome.get()}.")
    }
}

java {
    /*
        If you need to verify that the default setting: jvmTargetValidationMode = JvmTargetValidationMode.ERROR
        works, uncomment this code AND uncomment the compiler options jvmTarget in the kotlin extension.
        You should still see:

            Execution failed for task ':list:compileKotlin'.
            > Inconsistent JVM-target compatibility detected for tasks 'compileJava' (15) and 'compileKotlin' (21).
    */
    // targetCompatibility = JavaVersion.VERSION_15

    /*
        By setting this here, any kotlin tasks will also pick this up.

        See: https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains-support
    */
    toolchain {
        vendor = JvmVendorSpec.MICROSOFT
        languageVersion = javaLanguageVersion
    }
}

kotlin {
    /*
        Uncomment this + the properties in gradle.properties to run using k2 compiler
     */
    // sourceSets.all {
    //     languageSettings {
    //         languageVersion = "2.0"
    //     }
    // }

    /*
        Uncomment this + targetCompatibility in the java extension if you need to verify
        that the default setting: jvmTargetValidationMode = JvmTargetValidationMode.ERROR
        is worked (default behaviour)
     */
    // compilerOptions {
    //     jvmTarget = JvmTarget.JVM_1_8
    // }
}

tasks.withType<JavaCompile>().configureEach {
    /*
        The kotlin language spec uses this by default but this
        protects us against any java compilation tasks not using it.
     */
    options.encoding = "UTF-8"

    /*
        Setting the release flag ensures the specified language level is used regardless of which compiler actually
        performs the compilation.

        For more details: https://docs.gradle.org/current/userguide/building_java_projects.html#sec:compiling_with_release
     */
    options.release.set(getLazilyEvaluatedValue(javaLanguageVersion.get().asInt()))

    /*
        Runs the compiler as a separate process

        Specifically:

        "Gradle reuses this process within the duration the build, so the forking overhead is minimal.
        By forking memory-intensive compilation into a separate process, we minimize garbage collection in the main
        Gradle process. Less garbage collection means that Gradle’s infrastructure can run faster, especially when
        you also use parallel builds."

        For more details: https://docs.gradle.org/current/userguide/performance.html#optimize_the_compiler
     */

    options.isFork = false

    /*
        Ensures that incremental compilation is absolutely OFF and is not being overriden somehow.

        This is due to noticed issues with caching when it was on.
     */
    options.isIncremental = false

    /*
        Is kept consistent with gradle.properties to ensure that single use daemon's can be made if we need them
     */
    options.forkOptions.jvmArgs = listOf("-Xmx2g", "-XX:MaxMetaspaceSize=384m", "-Dfile.encoding=UTF-8", "-XX:+HeapDumpOnOutOfMemoryError")
}

/*
    Ensures that jar is included properly for anyone who overrides destination folder
    See: https://kotlinlang.org/docs/gradle-configure-project.html#non-default-location-of-compile-tasks-destinationdirectory
 */

tasks.jar {
    from(getLazilyEvaluatedValue(sourceSets.main.get().output ))
    from(getLazilyEvaluatedValue(sourceSets.main.get().kotlin.classesDirectory ))
}

// See: https://docs.gradle.org/current/userguide/working_with_files.html#sec:reproducible_archives
tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = true
    isReproducibleFileOrder = true
}


tasks.test {
    useJUnitPlatform()
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)

    doFirst {
        logger.lifecycle("Starting tests")
    }

    doLast {
        logger.lifecycle("Finishing tests")
    }
}

tasks.register("compileAll") {
    group = LifecycleBasePlugin.BUILD_GROUP
    description = "Compile all Java code"

    dependsOn(tasks.check)

}

tasks.register("printDependencies") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Prints dependencies by configuration"

    configurations.forEach { config ->
        if (config.isCanBeResolved) {
            logger.lifecycle("\nConfiguration dependencies: ${config.name}")
            try {
                config.resolvedConfiguration.resolvedArtifacts.forEach { artifact ->
                    logger.lifecycle(" - ${artifact.moduleVersion.id.group}:${artifact.name}:${artifact.moduleVersion.id.version}")
                }
            } catch (e: Exception) {
                logger.lifecycle("Failed to resolve ${config.name}: ${e.message}")
            }
        }
    }
}