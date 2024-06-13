plugins {
    kotlin("jvm")
    `maven-publish`
}


publishing {
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
    finalizedBy("publish")
}
