

plugins {
    `kotlin-dsl`
}

// configurations.all {
//     resolutionStrategy {
//         force("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3")
//         force("org.jetbrains.kotlin:kotlin-reflect:1.9.20")
//         failOnVersionConflict()
//     }
// }

dependencies {
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))

    implementation(project(":commons-plugin"))
    implementation(project(":capability-conflict-avoidance-plugin"))

    implementation("com.autonomousapps:dependency-analysis-gradle-plugin") {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-common")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-scripting-common")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-scripting-jvm")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-compiler-embeddable")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-daemon-client")
        exclude(group = "org.checkerframework", module = "checker-qual")
        exclude(group = "com.google.errorprone", module = "error_prone_annotations")
        exclude(group = "com.github.ben-manes.caffeine", module = "caffeine")
    }

    // api("org.springframework.boot:spring-boot-dependencies") {
    //     exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
    // }

    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin")
}
