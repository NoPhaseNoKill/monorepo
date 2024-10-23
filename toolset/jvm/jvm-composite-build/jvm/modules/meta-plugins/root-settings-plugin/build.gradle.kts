
plugins {
    `java-gradle-plugin`
    id("org.gradle.maven-publish")
    id("org.jetbrains.kotlin.jvm") version ("2.1.0-Beta1")
}

kotlin {
    jvmToolchain(21)
}

group = "com.nophasenokill"
version = "1.0.0-local"

gradlePlugin {
    plugins {
        create("rootSettingsPlugin") {
            id = "com.nophasenokill.root-settings-plugin"
            implementationClass = "RootSettingsPlugin"
        }

        create("mavenLocalRepoPlugin") {
            id = "com.nophasenokill.maven-local-repo-plugin"
            implementationClass = "MavenLocalRepoPlugin"
        }
    }
    isAutomatedPublishing = false
}

repositories {
    gradlePluginPortal()
}

buildscript {
    repositories {
        gradlePluginPortal()
        maven(providers.environmentVariable("PUBLISHING_REPOSITORY_URL").getOrElse("/home/gardo/projects/monorepo/toolset/jvm/jvm-composite-build/jvm/local-repo"))
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0-Beta1")
    }

    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1")
        }
    }
}

subprojects {
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1")
        }
    }
}


//
publishing.publications.create<MavenPublication>("mavenJava") {


    from(components["java"])
    versionMapping { allVariants { fromResolutionResult() } }
}

publishing.repositories {

    maven(providers.environmentVariable("PUBLISHING_REPOSITORY_URL").getOrElse("${rootDir}/../local-repo"))
}

// Because the jar is shared with the root build, this ensures we get a fresh copy each time the project changes
tasks.publish {
    dependsOn(tasks.jar)
}
//
dependencies {
//     implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1")
//     implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0-Beta1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1") {
        isTransitive = false
    }
    implementation("com.gradle:develocity-gradle-plugin:3.18.1") {
        isTransitive = false
    }
    implementation("org.gradle.toolchains:foojay-resolver:0.8.0") {
        isTransitive = false
    }
}
//
//
//
