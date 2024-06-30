plugins {
    id("java-platform")
}

group = "com.nophasenokill.platforms"


// Do not use this. This includes the dependencies
// javaPlatform.allowDependencies()

dependencies {
    constraints {
        api("org.jetbrains.kotlin:kotlin-bom:${libs.versions.kotlin.get()}")
        api("org.jetbrains.kotlin:kotlin-reflect:${libs.versions.kotlin.get()}")
        api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${libs.versions.kotlin.get()}")
        api("org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}")

        api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${libs.versions.coroutines.get()}")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-bom:${libs.versions.coroutines.get()}")

        api("org.slf4j:slf4j-api:${libs.versions.slf4j.get()}")
        api("org.slf4j:slf4j-simple:${libs.versions.slf4j.get()}")

        api("org.gradle:gradle-tooling-api:${libs.versions.gradle.get()}") {
            because("It matches version of gradle being used at root")
        }

        api("commons-io:commons-io:${libs.versions.commonsIo.get()}")

        api("org.junit:junit-bom:${libs.versions.junit.get()}")
    }
}
