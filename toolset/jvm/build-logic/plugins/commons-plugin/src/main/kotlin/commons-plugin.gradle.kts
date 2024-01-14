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
    /*
        Setting the release flag ensures the specified language level is used regardless of which compiler actually performs the compilation.
        For more details: https://docs.gradle.org/current/userguide/building_java_projects.html#sec:compiling_with_release
     */
    options.release.set(javaLanguageVersion.asInt())
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