plugins {
    `maven-publish`
    `kotlin-dsl`
    id("com.nophasenokill.producer-plugin") version("1.0.0-local-dev")
}

gradlePlugin {
    plugins {
        create("consumer-plugin") {
            id = "com.nophasenokill.consumer-plugin"
            implementationClass = "com.nophasenokill.ConsumerPlugin"
        }
    }
}

dependencies {
    implementation("com.nophasenokill:producer-plugin:1.0.0-local-dev")
    // implementation(project(":producer-plugin"))
    // testImplementation(project(path = ":producer-plugin", configuration = "testDependencyImplementationConfiguration"))
    // testRuntimeOnly(project(path = ":producer-plugin", configuration = "testDependencyRuntimeOnlyConfiguration"))
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
    dependsOn(":producer-plugin:build")
    mustRunAfter(":producer-plugin:build")
}

tasks.build {
    finalizedBy("publish")
}
