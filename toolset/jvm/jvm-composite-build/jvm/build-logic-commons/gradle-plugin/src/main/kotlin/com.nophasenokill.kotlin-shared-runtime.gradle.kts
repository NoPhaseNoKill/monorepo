import com.nophasenokill.commons.configureJavaToolChain
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    kotlin("jvm")
    id("com.nophasenokill.module-jar")
    id("com.nophasenokill.reproducible-archives")
    id("com.nophasenokill.repositories")
    id("com.nophasenokill.code-quality")
    id("com.nophasenokill.detekt")
    id("com.nophasenokill.test-retry")
    id("com.nophasenokill.ci-reporting")
    id("com.nophasenokill.private-javadoc")
}

description = "A plugin that sets up a Kotlin DSL code that is shared between build-logic and runtime"

java {
    configureJavaToolChain()
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    compilerOptions {
        allWarningsAsErrors = true
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

detekt {
    // overwrite the config file's location
    config.convention(project.isolated.rootProject.projectDirectory.file("../gradle/detekt.yml"))
}
