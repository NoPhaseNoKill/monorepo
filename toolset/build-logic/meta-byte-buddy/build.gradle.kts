
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("java-library")
    alias(libs.plugins.kotlinJvm)
}

dependencies {

    implementation(gradleApi())
    testImplementation("org.junit.jupiter:junit-jupiter-api:${libs.versions.junit.get()}")

    /*
        These are required, so we don't implicitly load test framework. https://docs.gradle.org/8.7/userguide/upgrading_version_8.html#test_framework_implementation_dependencies
     */
    testImplementation("org.junit.jupiter:junit-jupiter:${libs.versions.junit.get()}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:${libs.versions.junitPlatform.get()}")

    implementation("net.bytebuddy:byte-buddy:${libs.versions.byteBuddy.get()}")
    implementation("net.bytebuddy:byte-buddy-agent:${libs.versions.byteBuddy.get()}")
    testImplementation("net.bytebuddy:byte-buddy-agent:${libs.versions.byteBuddy.get()}")
    testImplementation("net.bytebuddy:byte-buddy:${libs.versions.byteBuddy.get()}")

}

tasks {
    test {

        // Fixes: https://github.com/gradle/gradle/issues/18647
        jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")

        useJUnitPlatform()

        testLogging.events = setOf(
            TestLogEvent.STANDARD_OUT,
            TestLogEvent.STARTED,
            TestLogEvent.PASSED,
            TestLogEvent.SKIPPED,
            TestLogEvent.FAILED,
            TestLogEvent.STANDARD_OUT,
            TestLogEvent.STANDARD_ERROR,
        )
    }
}

tasks.jar {
    manifest {
        attributes(
            "Premain-Class" to "com.nophasenokill.TestExecutionTrackerAgent"
        )
    }
}

tasks.withType<Test> {
    val agentJar = tasks.jar.get().archiveFile.get().asFile.absolutePath
    jvmArgs = listOf("-javaagent:$agentJar")
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




