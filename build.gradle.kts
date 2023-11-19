
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm") version "1.9.10"
    id("idea")
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

allprojects {
    group = "com.integraboost"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }
}

val copyConf by tasks.creating(Copy::class) {
    from("${project.rootDir}/conf")
    into("${buildDir}/conf")
}

tasks.named("build") {
    dependsOn(copyConf)
}

subprojects {
    apply {
        plugin("kotlin")
    }

    dependencies {
        implementation("io.arrow-kt:arrow-core:1.2.0")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
        implementation(kotlin("stdlib-jdk8"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3")
    }

    buildDir = File("${rootProject.buildDir}/${project.name}")

    tasks.withType<Test> {
        dependsOn(":build")
        useJUnitPlatform()

        testLogging {
            events = setOf(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.STANDARD_OUT, TestLogEvent.STANDARD_ERROR)
            systemProperties["java.util.logging.config.file"] = "${rootProject.buildDir}/conf/logging.properties"

        }
    }
}
