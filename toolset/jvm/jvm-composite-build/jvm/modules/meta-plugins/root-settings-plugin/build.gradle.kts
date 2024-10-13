
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
    }
    isAutomatedPublishing = false
}

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

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0-Beta1")
    implementation("com.gradle:develocity-gradle-plugin:3.18.1")
}



