import com.integraboost.configureTestLogging

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

    configureTestLogging()
}
