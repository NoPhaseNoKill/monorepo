

plugins {
    `kotlin-dsl`
}

// configurations.all {
//     resolutionStrategy {
//         force("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3")
//         // force("org.jetbrains.kotlin:kotlin-reflect:1.9.20")
//         failOnVersionConflict()
//     }
// }

dependencies {
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))

    implementation(project(":commons-plugin"))



    implementation("io.fuchs.gradle.classpath-collision-detector:classpath-collision-detector")
    implementation("org.gradlex:java-ecosystem-capabilities")
}