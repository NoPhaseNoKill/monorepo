plugins {
    kotlin("jvm") version "1.8.21"
}

allprojects {
    group = "com.integraboost"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
    }

    tasks.test {
        useJUnitPlatform()

        /**
         * Run junit tests in parallel
         */
        systemProperties["junit.jupiter.execution.parallel.enabled"] = true
        systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
        systemProperties["junit.jupiter.execution.parallel.mode.classes.default"] = "concurrent"
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1

        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}
