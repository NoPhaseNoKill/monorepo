

plugins {
    id("java-platform")
    id("dependency-analysis-platform")
}

group = "com.nophasenokill.platform"

// Do not use this. This includes the dependencies
// javaPlatform.allowDependencies()


dependencies {
    constraints {

        api("com.autonomousapps:dependency-analysis-gradle-plugin:1.29.0")
        api("com.google.errorprone:error_prone_annotations:2.13.1")
        api("io.fuchs.gradle.classpath-collision-detector:classpath-collision-detector:0.3")
        api("jakarta.activation:jakarta.activation-api:1.2.2")
        api("javax.activation:activation:1.1.1")
        api("org.apache.commons:commons-text:1.11.0")
        api("commons-io:commons-io:2.5")
        api("commons-codec:commons-codec:1.9")
        api("org.checkerframework:checker-qual:3.21.4")
        api("org.gradlex:java-ecosystem-capabilities:1.4")
        api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.20")
        api("org.jetbrains.kotlin:kotlin-bom:1.9.20") {
            because("It matches the version of 3.2.0 for spring")
        }

        api("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")

        api("org.junit:junit-bom:5.10.1")

        api("org.springframework.boot:spring-boot-dependencies:3.2.0") {
            because("It matches the version of 1.9.20 for kotlin bom")
        }






    }
}