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

// See: https://kotlinlang.org/docs/gradle-configure-project.html#check-for-jvm-target-compatibility-of-related-compile-tasks
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    jvmTargetValidationMode.set(org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode.ERROR)
}

java {
    toolchain{
        vendor = JvmVendorSpec.MICROSOFT
        languageVersion = javaLanguageVersion.get()
    }
}

kotlin {
    jvmToolchain {
        vendor = JvmVendorSpec.MICROSOFT
        languageVersion = javaLanguageVersion.get()
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    /*
        Setting the release flag ensures the specified language level is used regardless of which compiler actually performs the compilation.
        For more details: https://docs.gradle.org/current/userguide/building_java_projects.html#sec:compiling_with_release
     */
    options.release.set(javaLanguageVersion.get().asInt())

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
    from(sourceSets.main.get().output)
    from(sourceSets.main.get().kotlin.classesDirectory)
}

// See: https://docs.gradle.org/current/userguide/working_with_files.html#sec:reproducible_archives
tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

// Configure common dependencies for all projects
dependencies {
    // this allows use to declare non-versioned dependencies inside each project
    // ie: implementation("org.apache.commons:commons-text")
    implementation(platform("com.nophasenokill.platform:platform"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register("compileAll") {
    group = LifecycleBasePlugin.BUILD_GROUP
    description = "Compile all Java code"
    dependsOn(tasks.withType<JavaCompile>())
}

tasks.register("testAll") {
    group = LifecycleBasePlugin.BUILD_GROUP
    description = "Test all Java code"
    dependsOn(tasks.withType<Test>())
}