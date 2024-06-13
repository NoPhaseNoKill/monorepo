

plugins {
    `maven-publish`
    `kotlin-dsl`
}

group = "com.nophasenokill"
version = "1.0.0-local-dev"


dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${embeddedKotlinVersion}")
//     implementation(gradleApi())
//     testImplementation(gradleTestKit())
//
//
//     testImplementation(platform("org.junit:junit-bom:5.10.1"))
//     testImplementation("org.junit.jupiter:junit-jupiter")
//     testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

publishing {
    repositories {
        maven {
            url = uri("${rootProject.projectDir}/local-repo")
        }
    }
}

tasks.build {
    finalizedBy("publish")
}