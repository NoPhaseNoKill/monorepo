plugins {
    id("java-platform")
}

group = "com.nophasenokill.platform"

javaPlatform.allowDependencies()

dependencies {
    api(platform("org.jetbrains.kotlin:kotlin-bom:1.9.20"))

    api(platform("org.junit:junit-bom:5.10.0"))

    constraints {
        api("com.autonomousapps:dependency-analysis-gradle-plugin:1.28.0")
        api("io.fuchs.gradle.classpath-collision-detector:classpath-collision-detector:0.3")
        api("org.gradlex:java-ecosystem-capabilities:1.3.1")
        api("jakarta.activation:jakarta.activation-api:1.2.2")
        api("javax.activation:activation:1.1.1")

        api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.20")
        api("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")

        api("org.gradle.toolchains:foojay-resolver:0.7.0")
        api("com.gradle.enterprise:com.gradle.enterprise.gradle.plugin:3.16.1")
        api( "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        api("org.apache.commons:commons-text:1.10.0")
    }
}