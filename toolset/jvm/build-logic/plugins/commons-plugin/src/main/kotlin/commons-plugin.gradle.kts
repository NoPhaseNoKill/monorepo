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

kotlin {
    /*
        Uncomment this + the properties in gradle.properties to run using k2 compiler
     */
    // sourceSets.all {
    //     languageSettings {
    //         languageVersion = "2.0"
    //     }
    // }

    jvmToolchain {
        vendor = JvmVendorSpec.MICROSOFT
        languageVersion = javaLanguageVersion
    }
}

tasks.withType<JavaCompile>().configureEach {
    /*
        If you need to verify that the default setting: jvmTargetValidationMode = JvmTargetValidationMode.ERROR
        works, uncomment this code and you should still see:

            Execution failed for task ':list:compileKotlin'.
            > Inconsistent JVM-target compatibility detected for tasks 'compileJava' (15) and 'compileKotlin' (21).
     */
    // targetCompatibility = "15"

    options.encoding = "UTF-8"
    /*
        Setting the release flag ensures the specified language level is used regardless of which compiler actually performs the compilation.
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

    options.isFork = true
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
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

// Configure common dependencies for all projects
dependencies {
    // this allows us to declare non-versioned dependencies inside each project
    // ie: implementation("org.apache.commons:commons-text")
    implementation(platform("com.nophasenokill.platform:platform"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)


    doFirst {
        println("Starting tests")
    }

    doLast {
        println("Finishing tests")
    }
}

tasks.register("compileAll") {
    group = LifecycleBasePlugin.BUILD_GROUP
    description = "Compile all Java code"
    dependsOn(tasks.withType<JavaCompile>())
}

tasks.register("testAll") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Test all Java code"
    dependsOn(tasks.withType<Test>())

    /*
        The useTarget method is used to replace a dependency before it is resolved. This is useful
        under the circumstance where we want a clear distinction between our out of date build script
         compiled plugins (locally cached) and our absolute most up-to-date build script compiled plugins
         (the code). This then ensures that dependencies when in local development are always using whatever
         is the latest code you have in it.
    */
    configurations.all {
        resolutionStrategy.dependencySubstitution.all {
            requested.let {
                if (it is ModuleComponentSelector && it.group == "com.nophasenokill") {
                    val targetProject = findProject(":${it.module}")
                    if (targetProject != null) {
                        useTarget(targetProject)
                    }
                }
            }
        }
    }
}

val printDependenciesTask = tasks.register("printDependencies") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Prints dependencies by configuration"

    configurations.forEach { config ->
        if (config.isCanBeResolved) {
            println("\nConfiguration dependencies: ${config.name}")
            try {
                config.resolvedConfiguration.resolvedArtifacts.forEach { artifact ->
                    println(" - ${artifact.moduleVersion.id.group}:${artifact.name}:${artifact.moduleVersion.id.version}")
                }
            } catch (e: Exception) {
                println("Failed to resolve ${config.name}: ${e.message}")
            }
        }
    }
}