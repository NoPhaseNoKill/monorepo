plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

version = "1.0.0-local-dev"
group = "com.nophasenokill"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {

    kotlinCompilerClasspath("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.23")
    kotlinCompilerClasspath("org.jetbrains.kotlin:kotlin-stdlib:1.9.23")
    kotlinCompilerClasspath("org.jetbrains:annotations:24.1.0")
    kotlinCompilerClasspath("org.jetbrains.kotlin:kotlin-script-runtime:1.9.23")
    kotlinCompilerClasspath("org.jetbrains.kotlin:kotlin-daemon-embeddable:1.9.23")
    kotlinCompilerClasspath("org.jetbrains.intellij.deps:trove4j:1.0.20200330")

    kotlinCompilerPluginClasspathMain("org.jetbrains.kotlin:kotlin-scripting-compiler-impl-embeddable:1.9.23")
    kotlinCompilerPluginClasspathMain("org.jetbrains.kotlin:kotlin-scripting-common:1.9.23")
    kotlinCompilerPluginClasspathMain("org.jetbrains.kotlin:kotlin-scripting-jvm:1.9.23")

    kotlinCompilerPluginClasspathTest("org.jetbrains.kotlin:kotlin-scripting-compiler-impl-embeddable:1.9.23")
    kotlinCompilerPluginClasspathTest("org.jetbrains.kotlin:kotlin-scripting-common:1.9.23")
    kotlinCompilerPluginClasspathTest("org.jetbrains.kotlin:kotlin-scripting-jvm:1.9.23")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2") // may be required in future when using junit params
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.opentest4j:opentest4j:1.3.0")
    testImplementation("org.junit.platform:junit-platform-commons:1.10.2")
    testImplementation("org.junit.platform:junit-platform-engine:1.10.2")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")
}