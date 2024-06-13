plugins {
    `maven-publish`
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("producer-plugin") {
            id = "com.nophasenokill.producer-plugin"
            implementationClass = "com.nophasenokill.ProducerPlugin"
        }
    }
}

dependencies {

}

version = "1.0.0-local-dev"
group = "com.nophasenokill"

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
