

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
    // this allows us to declare non-versioned dependencies inside each project
    // ie: implementation("org.apache.commons:commons-text")
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin")
}