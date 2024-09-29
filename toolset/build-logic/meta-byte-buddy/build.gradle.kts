
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.jvm.tasks.Jar

plugins {
    alias(libs.plugins.kotlinJvm)
}

group = "com.nophasenokill"
version = "0.1.local-dev"

dependencies {

    implementation("com.google.guava:guava:${libs.versions.guava.get()}")

    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation(gradleApi())
    implementation("org.junit.jupiter:junit-jupiter-api:${libs.versions.junit.get()}")

    /*
        These are required, so we don't implicitly load test framework. https://docs.gradle.org/8.7/userguide/upgrading_version_8.html#test_framework_implementation_dependencies
     */
    implementation("org.junit.jupiter:junit-jupiter:${libs.versions.junit.get()}")
    implementation("org.junit.platform:junit-platform-launcher:${libs.versions.junitPlatform.get()}")

    implementation("net.bytebuddy:byte-buddy:${libs.versions.byteBuddy.get()}")
    implementation("net.bytebuddy:byte-buddy-agent:${libs.versions.byteBuddy.get()}")

}

tasks.withType(Jar::class.java).configureEach {
    manifest = java.manifest {
        manifestContentCharset = "UTF-8"
        isZip64 = true
        attributes(
            "Premain-Class" to "com.nophasenokill.TestExecutionTrackerAgent"
        )
    }

    val file = layout.buildDirectory.file("byte-buddy-manifest.MF")

    doLast {
        manifest.writeTo(file.get().asFile)
    }
}


tasks.withType(Test::class.java).configureEach {

    useJUnitPlatform()

    testLogging.events = setOf(
        TestLogEvent.STARTED,
        TestLogEvent.PASSED,
        TestLogEvent.SKIPPED,
        TestLogEvent.FAILED,
        TestLogEvent.STANDARD_OUT,
        TestLogEvent.STANDARD_ERROR,
    )

    // val agentJar = tasks.jar.get().archiveFile.get().asFile.absolutePath

    val agentJar = tasks.jar.get().archiveFile.get().asFile.absolutePath
    val testClassesDir = sourceSets["test"].output.classesDirs.asFileTree.files
    val testClassPaths = testClassesDir.joinToString(separator = File.pathSeparator) { it.absolutePath }


    // val testClassesDir = configurations.runtimeElements.map { it.files.toSet() }
    // val testClassPaths = testClassesDir.get().joinToString(separator = File.pathSeparator) { it.absolutePath }


    /*
        --add-opens=java.base/java.lang=ALL-UNNAMED
        used to fix: https://github.com/gradle/gradle/issues/18647
     */
    jvmArgs = listOf("-javaagent:$agentJar", "--add-opens=java.base/java.lang=ALL-UNNAMED", "-Dtest.classpath=$testClassPaths")
}

// tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
//     jvmTargetValidationMode.set(org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode.ERROR)
// }

// kotlin {
//     jvmToolchain {
//         languageVersion.set(JavaLanguageVersion.of(21))
//         vendor.set(JvmVendorSpec.MICROSOFT)
//     }
// }
//
// kotlin {
//     compilerOptions {
//         kotlinDaemonJvmArgs = listOf("-Xmx1500m" ,"-Xms500m")
//     }
// }
//
// tasks {
//     compileKotlin {
//         useDaemonFallbackStrategy.set(false)
//     }
// }
//
// tasks.withType<CompileUsingKotlinDaemon>().configureEach {
//     compilerExecutionStrategy.set(KotlinCompilerExecutionStrategy.DAEMON)
//     kotlinDaemonJvmArguments.set(listOf("-Xmx1500m" ,"-Xms500m"))
// }




