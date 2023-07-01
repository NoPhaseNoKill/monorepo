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
    apply {
        plugin("kotlin")
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
    }

    tasks.withType<Test> {

        /*
            system properties should be set before jvm starts
            this dampens the log level for unnecessary junit logs
         */
        systemProperties["java.util.logging.config.file"] = "${project.buildDir}/resources/test/logging.properties"

        useJUnitPlatform()

        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}
