plugins {
    id("base")
    id("java")
    id("org.jetbrains.kotlin.jvm")
}

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

// Configure common test runtime dependencies for all projects
dependencies {
    // this allows use to declare non-versioned dependencies inside each project
    // ie: implementation("org.apache.commons:commons-text")
    implementation(platform("com.nophasenokill.platform:plugins-platform"))
    implementation(platform("com.nophasenokill.platform:test-platform"))

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