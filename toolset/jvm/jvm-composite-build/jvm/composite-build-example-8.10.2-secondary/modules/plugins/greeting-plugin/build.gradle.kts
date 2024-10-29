plugins {
    id("java-gradle-plugin")
    id("maven-publish")
}

group = "org.sample"
version = "1.0-SNAPSHOT"

gradlePlugin {
    plugins {
        create("customPlugin") {
            id = "org.sample.greeting"
            implementationClass = "org.sample.GreetingPlugin"
        }
    }
}

configurations {
    create("pluginUnderTest") {
        isCanBeConsumed = true
        isCanBeResolved = true
        extendsFrom(configurations.runtimeClasspath.get())
    }
}

publishing {
    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("repo"))
        }
    }
}

// tasks.register<Jar>("pluginUnderTestJar") {
//     archiveClassifier.set("pluginUnderTest")
//     from(sourceSets.main.get().output)
//     configurations["pluginUnderTest"].outgoing.artifact(archiveFile)
// }
//
// tasks.register<Jar>("buildPluginJar") {
//     archiveBaseName.set("greeting-plugin")
//     archiveVersion.set("1.0-SNAPSHOT")
//     from(sourceSets.main.get().output)
// }


