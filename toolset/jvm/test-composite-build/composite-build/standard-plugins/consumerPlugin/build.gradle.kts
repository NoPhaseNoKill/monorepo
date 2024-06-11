import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency

plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm") version "1.9.23"
}

gradlePlugin {
    plugins {
        create("consumerPlugin") {
            id = "com.nophasenokill.consumer"
            implementationClass = "com.nophasenokill.ConsumerPlugin"
        }
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("${rootProject.projectDir}/local-repo")
    }
}

dependencies {
    implementation(project(":producerPlugin"))

    testImplementation(project(path = ":producerPlugin", configuration = "testDependencyImplementationConfiguration"))
    testRuntimeOnly(project(path = ":producerPlugin", configuration = "testDependencyRuntimeOnlyConfiguration"))

    // implementation(DefaultExternalModuleDependency("com.nophasenokill", "producerPlugin", "1.0.0-SNAPSHOT"))
    // implementation(DefaultExternalModuleDependency("com.nophasenokill", "producerPlugin", "1.0.0-SNAPSHOT", "testDependencyConfiguration"))
    // testImplementation("org.jetbrains.kotlin:kotlin-test")
    // testImplementation(DefaultExternalModuleDependency("com.nophasenokill", "producerPlugin", "1.0.0-SNAPSHOT", "testImplementation"))
    // implementation("com.nophasenokill:producerPlugin:1.0.0-SNAPSHOT")
    // compileOnly("com.nophasenokill:producerPlugin:1.0.0-SNAPSHOT")
    // testImplementation("com.nophasenokill:producerPlugin:1.0.0-SNAPSHOT")
}

version = "1.0.0-SNAPSHOT"
group = "com.nophasenokill"

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("${rootProject.projectDir}/local-repo")
        }
    }
}

tasks.test {
    useJUnitPlatform()
    dependsOn(":producerPlugin:publishMavenJavaPublicationToMavenRepository")
}

tasks.build {
    finalizedBy("publishMavenJavaPublicationToMavenRepository")
}
