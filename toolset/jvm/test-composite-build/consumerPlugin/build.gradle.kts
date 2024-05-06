
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

// repositories {
//     // maven {
//     //     url = uri("${rootProject.projectDir}/local-repo")
//     // }
//     mavenCentral()
// }

dependencies {
    /*
        Equivalent of: implementation(project(":producerPlugin"))
     */

    // project.dependencies.add(
    //     "implementation",
    //     project.dependencies.project(mapOf("path" to ":producerPlugin"))
    // )

    /*
        Equivalent of: implementation("com.nophasenokill:producerPlugin:1.0.0-local-dev")
     */

    project.dependencies.add(
        "implementation",
        project.dependencies.create("com.nophasenokill","producerPlugin", "1.0.0-local-dev")
    )

    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

version = "1.0.0-local-dev"
group = "com.nophasenokill"


tasks.test {
    useJUnitPlatform()
}
