plugins {
    // id("com.nophasenokill.commons")
    id("org.jetbrains.kotlin.jvm")
    id("java-library")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    jvmTargetValidationMode.set(org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode.WARNING)
}
