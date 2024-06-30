
plugins {
    kotlin("jvm")
    id("java-library")
    `maven-publish`
    id("com.nophasenokill.build-service-warning-fix-plugin")
}

group = "com.nophasenokill.domain"
version = "0.1.local-dev"

/*
    If you need access to the libs you need to do:

    afterEvaluate {

            val versionCatalog = extensions.findByType(VersionCatalogsExtension::class.java)?.named("libs")
            val kotlinVersion = requireNotNull(versionCatalog?.findVersion("kotlin")?.get())

            dependencies {
                implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${kotlinVersion}")
            }

    }
 */

dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${embeddedKotlinVersion}")
    implementation(project(":account"))
}

publishing {
    repositories {
        maven {
            setUrl(file("../../../local-repo"))
        }
    }
    publications {
        create<MavenPublication>("maven") { from(components["java"]) }
    }
}