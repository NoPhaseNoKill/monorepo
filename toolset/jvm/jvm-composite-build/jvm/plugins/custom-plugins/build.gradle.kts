plugins {
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.nophasenokill"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13")
}

gradlePlugin {
    plugins {
        /*
            BEWARE:
                Name specifically in this context is NOT the same as project.name a
                nd WILL catch you out if you're not careful
         */
        create(lowerCaseName("${group}.CustomGreetingPlugin")) {
            id = "${group}.${project.name}.greeting" // project.name !== name in this context
            implementationClass = "${group}.CustomGreetingPlugin"
        }
    }
}

fun lowerCaseName (name: String): String {
    return name.replaceFirstChar { it.lowercase() }
}

publishing {
    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("repo"))
        }
    }
}



