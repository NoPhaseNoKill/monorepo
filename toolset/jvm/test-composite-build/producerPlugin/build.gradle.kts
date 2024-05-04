plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm") version "1.9.23"
}

gradlePlugin {
    plugins {
        create("producerPlugin") {
            id = "com.nophasenokill.producer"
            implementationClass = "com.nophasenokill.ProducerPlugin"
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
    testImplementation("org.jetbrains.kotlin:kotlin-test")
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
}

tasks.build {
    finalizedBy("publishMavenJavaPublicationToMavenRepository")
}
