plugins {
    id("base")
    id("java")
    id("org.jetbrains.kotlin.jvm")
}

// Configure Java/Kotlin compilation on java/kotlin {} extension or directly on 'JavaCompile' tasks
val javaLanguageVersion = JavaLanguageVersion.of(17)

java {
    toolchain.languageVersion.set(javaLanguageVersion)
}

kotlin {
    jvmToolchain {
        languageVersion.set(javaLanguageVersion)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

dependencies {
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