plugins {
    id("java-platform")
}

pluginManager.withPlugin("java-platform") {

    group = "com.nophasenokill.platform"

    // Do not use this. This includes the dependencies
    // javaPlatform.allowDependencies()

    dependencies {
        constraints {

            api("com.autonomousapps:dependency-analysis-gradle-plugin:1.29.0")
            api("com.google.code.gson:gson:2.10.1")
            api("com.google.errorprone:error_prone_annotations:2.13.1")

            api("io.github.cdsap:talaiot:2.0.3")
            api("io.github.cdsap.talaiot:talaiot:2.0.3")
            api("io.github.cdsap.talaiot:io.github.cdsap.talaiot.gradle.plugin:2.0.3")
            api("jakarta.activation:jakarta.activation-api:1.2.2")
            api("javax.activation:activation:1.1.1")
            api("org.apache.commons:commons-text:1.11.0")
            api("commons-io:commons-io:2.5")
            api("commons-codec:commons-codec:1.9")
            api("org.checkerframework:checker-qual:3.21.4")
            api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${libs.versions.kotlin.get()}")

            api("org.jetbrains.kotlin:kotlin-test-junit5:${libs.versions.kotlin.get()}")

            api("org.slf4j:slf4j-simple:${libs.versions.slf4j.get()}")
            api("org.gradle:gradle-tooling-api:${libs.versions.gradle.get()}") {
                because("It matches version of gradle being used")
            }

            api("org.jetbrains.kotlin:kotlin-bom:${libs.versions.kotlin.get()}") {
                because("It matches the version of 3.2.0 for spring")
            }

            api("org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}")

            api("org.junit:junit-bom:5.10.1")

        }
    }
}