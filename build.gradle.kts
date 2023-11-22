
plugins {
    kotlin("jvm") version "1.8.20"
    id("idea")
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("kotlin")
    }

    dependencies {
        val junitVersion = "5.10.1"
        // TODO try and fix issue with 1.7.3, which relates to classpath -  potentially being caused by build process
        val kotlinCoroutinesVersion = "1.6.4"

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${kotlinCoroutinesVersion}")
        implementation("org.junit.jupiter:junit-jupiter:${junitVersion}")

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${kotlinCoroutinesVersion}")
    }

    buildDir = File("${rootProject.buildDir}/${project.name}")

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
