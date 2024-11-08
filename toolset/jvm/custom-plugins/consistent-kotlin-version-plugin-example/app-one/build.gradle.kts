plugins {
    id("java")
    id("application")
    kotlin("jvm")
}

dependencies {
    implementation(project(":library-one"))
    implementation("org.slf4j:slf4j-api:2.0.16")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.16")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")
}

tasks.test {
    useJUnitPlatform()
}


tasks.named<JavaExec>("run") {
    mainClass.set("mypackage.App")
    classpath = files(tasks.named<JavaCompile>("compileJava").flatMap { it.destinationDirectory }, configurations.runtimeClasspath)
    doLast {
        classpath.files.forEach { println("Classpath file included from: ${it.path}") }
    }
}
