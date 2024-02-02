

plugins {
    id("java-platform")
    id("dependency-analysis-platform")
}

group = "com.nophasenokill.platform"

javaPlatform.allowDependencies()

dependencies {

    api(platform("org.jetbrains.kotlin:kotlin-bom:1.9.20"))  {
        because("It matches the version of 3.2.0 for spring")
    }

    api(platform("org.junit:junit-bom:5.10.1"))

    api(platform("org.springframework.boot:spring-boot-dependencies:3.2.0"))  {
        because("It matches the version of 1.9.20 for kotlin bom")
    }

    constraints {
        api("com.autonomousapps:dependency-analysis-gradle-plugin:1.29.0")
        api("com.gradle.enterprise:com.gradle.enterprise.gradle.plugin")
        // api("io.fuchs.gradle.classpath-collision-detector:classpath-collision-detector")
        api("org.apache.commons:commons-text:1.11.0")
        api("org.gradle.toolchains:foojay-resolver")
        api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.20")
        api("org.jetbrains.kotlin:kotlin-bom")

        api("org.junit:junit-bom")
        api("org.junit.jupiter:junit-jupiter-api")
        api("org.springframework.boot:spring-boot-dependencies")
    }
}