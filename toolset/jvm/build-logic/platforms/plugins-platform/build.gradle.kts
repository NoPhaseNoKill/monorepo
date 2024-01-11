plugins {
    id("java-platform")
}

group = "com.nophasenokill.platform"

configurations.api.get().dependencies.map { d -> println("${d.toDeclaredString()}") }
configurations.runtime.get().dependencies.map { d -> d.toDeclaredString() }

fun Dependency.toDeclaredString() = "$group:$name:$version"
fun DependencyConstraint.toDeclaredString() = "$group:$name:$version"

javaPlatform.allowDependencies()

dependencies {

    api(platform("org.jetbrains.kotlin:kotlin-bom:1.9.20"))  {
        because("It matches the version of 3.2.0 for spring")
    }

    api(platform("org.springframework.boot:spring-boot-dependencies:3.2.0"))  {
        because("It matches the version of 1.9.20 for kotlin bom")
    }

    constraints {
        api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.20")
        api("org.jetbrains.kotlin:kotlin-stdlib-common:1.9.20")
        api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.20")

        api("org.apache.commons:commons-text:1.10.0")

        // api("org.jetbrains.kotlin.android:org.jetbrains.kotlin.android.gradle.plugin:1.9.20")
        // api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin")


        api("com.autonomousapps:dependency-analysis-gradle-plugin:1.28.0")
        api("io.fuchs.gradle.classpath-collision-detector:classpath-collision-detector:0.3")
        api("org.gradlex:java-ecosystem-capabilities:1.4")
        api("jakarta.activation:jakarta.activation-api:1.2.2")
        api("javax.activation:activation:1.1.1")

        api("org.gradle.toolchains:foojay-resolver:0.7.0")
        api("com.gradle.enterprise:com.gradle.enterprise.gradle.plugin:3.16.1")
        api( "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        api("org.apache.commons:commons-text:1.10.0")
    }
}
