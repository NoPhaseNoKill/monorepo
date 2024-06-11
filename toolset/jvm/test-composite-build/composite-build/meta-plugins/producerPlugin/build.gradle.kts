plugins {
    `java-gradle-plugin`
    `maven-publish`
    `kotlin-dsl`
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

val testDependencyImplementationConfiguration: Configuration by configurations.creating {
    extendsFrom(configurations.testImplementation.get())
    isCanBeResolved = false
    isCanBeConsumed = true
}

val testDependencyRuntimeOnlyConfiguration: Configuration by configurations.creating {
    extendsFrom(configurations.testRuntimeOnly.get())
    isCanBeResolved = false
    isCanBeConsumed = true
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.23"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")

    testDependencyImplementationConfiguration(platform("org.junit:junit-bom:5.10.1"))
    testDependencyImplementationConfiguration("org.junit.jupiter:junit-jupiter")
    testDependencyRuntimeOnlyConfiguration("org.junit.platform:junit-platform-launcher")
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
