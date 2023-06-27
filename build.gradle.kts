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

        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}
