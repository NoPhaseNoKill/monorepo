plugins {
    kotlin("jvm") version "1.8.10"
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ow2.asm:asm:9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

tasks.register<Jar>("agentJar") {
    archiveClassifier.set("agent")
    manifest {
        attributes["Premain-Class"] = "com.example.Agent"
        attributes["Can-Redefine-Classes"] = "true"
    }
    from(sourceSets.main.get().output)
}

tasks.test {
    dependsOn(tasks.named("agentJar"))

    doFirst {
        val agentJar = tasks.named<Jar>("agentJar").get().archiveFile.get().asFile.absolutePath
        jvmArgs("-javaagent:$agentJar")
    }
    useJUnitPlatform()
}
