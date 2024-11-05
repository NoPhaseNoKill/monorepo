import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinApiPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile


val implementation = configurations.create("implementation")
val api = configurations.create("api")
val runtimeOnly = configurations.create("runtimeOnly")
val compileClasspath = configurations.create("compileClasspath") {
    extendsFrom(implementation, api)
}

/*
    DO NOT DECLARE BUILD SCRIPT HERE
 */

dependencies {
    api("jakarta.activation:jakarta.activation-api")
    implementation("commons-io:commons-io")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}


/*
    This needs to be registered during build configuration, and hence cannot be moved to a custom
    task, as that would be registered during task configuration.

    See package org.jetbrains.kotlin.gradle.tasks.configurationKotlinCompileConfig for more details.
 */
project.plugins.apply(KotlinApiPlugin::class)
val kotlinApiPlugin = project.plugins.getPlugin(KotlinApiPlugin::class)
val kotlinJvmOptions = kotlinApiPlugin.createCompilerJvmOptions()
val kotlinJvmCompileTask = kotlinApiPlugin.registerKotlinJvmCompileTask("compileKotlin", project.name)
kotlinJvmCompileTask.configure {

    multiPlatformEnabled.set(true)

    destinationDirectory.set(layout.buildDirectory.dir("classes/kotlin"))
    source(layout.projectDirectory.dir("src"))

    // Set compiler options
    compilerOptions.apply {
        jvmTarget.set(JvmTarget.JVM_21) // Equivalent to sourceCompatibility and targetCompatibility in Java
        freeCompilerArgs.set(
            listOf(
                "-Xbackend-threads=0", // Enables multi-threaded compilation, reducing build time on multi-core CPUs.
                "-Xinline-classes", // Reduces memory usage by avoiding object allocation for lightweight wrapper types.
                "-Xno-param-assertions", // Skips null checks on parameters, reducing runtime overhead for argument validation.
                "-Xno-call-assertions", // Skips null checks on method calls, improving runtime performance.
                "-Xir-inliner", // Uses IR inliner, which produces optimized inline functions, reducing bytecode size and improving runtime efficiency.
                "-Xlambdas=indy", // Uses invokedynamic for lambdas, reducing the creation of synthetic classes and improving runtime performance.
                "-Xsam-conversions=indy", // Uses invokedynamic for SAM conversions, minimizing synthetic class creation and optimizing JVM efficiency.
                "-Xstring-concat=indy-with-constants", // Optimizes string concatenation with invokedynamic, improving efficiency on JVM 9+ targets.
                "-Xuse-fast-jar-file-system", // Speeds up JAR file access, reducing compilation time for projects with many dependencies.
                "-Xenable-incremental-compilation" // Enables incremental builds, compiling only changed files to reduce build time.
            )
        )
    }

    // TODO confirm whether we should be adding a pluginClasspath of our buildScript dependencies here?
    libraries = files(compileClasspath)
}

val compileKotlin = tasks.named<KotlinJvmCompile>("compileKotlin") {

    val task = kotlinJvmCompileTask.get()

    doLast {
        task.sources.files.forEach { println("Source file included from: ${it.path}") }
        task.libraries.files.forEach { println("Classpath file included from: ${it.path}") }
        logger.lifecycle("compileKotlin freeCompilerArgs: ${task.compilerOptions.freeCompilerArgs.get().joinToString(separator = ",")}")
        logger.lifecycle("compileKotlin jvmTarget: ${task.compilerOptions.jvmTarget.get()}")
    }
}

val compileJava = tasks.register<JavaCompile>("compileJava") {
    destinationDirectory.set(layout.buildDirectory.dir("classes/java"))
    val javaSpecificFiles = layout.projectDirectory.dir("src").asFileTree.matching { include("**/*.java") }
    source(javaSpecificFiles)

    sourceCompatibility = "21"
    targetCompatibility = "21"

    classpath = files(compileClasspath)

    doLast {
        source.files.forEach { println("Source file included from: ${it.path}") }
        classpath.files.forEach { println("Classpath file included from: ${it.path}") }
        println("compileJava source compatibility: ${sourceCompatibility}, target compatibility: ${targetCompatibility}")
    }
}

// Configuring API and runtime elements for outgoing artifacts
configurations.create("apiElements") {
    extendsFrom(api)
    outgoing {
        artifact(compileJava.flatMap { it.destinationDirectory })
        artifact(compileKotlin.flatMap { it.destinationDirectory })
    }
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_API))
    }
}
configurations.create("runtimeElements") {
    extendsFrom(implementation, runtimeOnly, api)
    outgoing {
        artifact(compileJava.flatMap { it.destinationDirectory })
        artifact(compileKotlin.flatMap { it.destinationDirectory })
    }
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
    }
}

tasks.register("build") {

    dependsOn(tasks.named("gatherBuildScriptDependencies"))
    dependsOn(tasks.named("gatherProjectDependencies"))

    dependsOn(compileKotlin, compileJava)
}
