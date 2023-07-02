import com.integraboost.configureTestLogging

plugins {
    kotlin("jvm") version "1.8.21"
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
        implementation(kotlin("stdlib-jdk8"))
    }

    configureTestLogging()
}
