import org.jetbrains.kotlin.gradle.tasks.CompileUsingKotlinDaemon
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilerExecutionStrategy


plugins {
    `maven-publish`
    `kotlin-dsl`
}

group = "com.nophasenokill"
version = "1.0.0-local-dev"


dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${embeddedKotlinVersion}")
}

publishing {
    repositories {
        maven {
            url = uri("${rootProject.projectDir}/local-repo")
        }
    }
}

tasks.build {
    finalizedBy("publish")
    dependsOn(printJvmArgsTask)
}

tasks {
    compileKotlin {
        /*
            By default, kotlin tries to have a fallback strategy set if the daemon fails.
            This can be memory intensive, but more importantly, can lead to non-deterministic
            builds which will invalidate cache.

            See: https://kotlinlang.org/docs/gradle-compilation-and-caches.html#kotlin-compiler-fallback-strategy
         */
        useDaemonFallbackStrategy.set(false)
    }
}

tasks.withType<CompileUsingKotlinDaemon>().configureEach {
    compilerExecutionStrategy.set(KotlinCompilerExecutionStrategy.DAEMON)

    doLast {
        logger.quiet("kotlin.daemon.jvmargs being used are: ${kotlinDaemonJvmArguments.get()}")
    }
}

kotlin {
    val jvmArgs = providers.gradleProperty("org.gradle.jvmargs")
    kotlinDaemonJvmArgs = jvmArgs.get().split(" ")
}


val printJvmArgsTask = tasks.register("printJvmArgsTask") {
    val jvmArgs = providers.gradleProperty("org.gradle.jvmargs")

    doLast {
        logger.quiet("org.gradle.jvmargs being used by gradle are: ${jvmArgs.get()}")
    }
}

