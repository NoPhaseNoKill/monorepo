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
    implementation("com.nophasenokill:producerPlugin:1.0.0-SNAPSHOT")
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
    dependsOn(":producerPlugin:publishMavenJavaPublicationToMavenRepository")
}

tasks.build {
    finalizedBy("publishMavenJavaPublicationToMavenRepository")
}
