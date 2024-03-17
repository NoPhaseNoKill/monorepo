plugins {
    id("java-gradle-plugin")

    alias(libs.plugins.kotlinDsl) apply false
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:${libs.versions.junit.get()}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

gradlePlugin {
    // Define the plugin
    val greeting by plugins.creating {
        id = "com.nophasenokill.plugins-new.plugin"
        implementationClass = "com.nophasenokill.StandalonePluginPlugin"
    }
}

// Add a source set for the functional test suite
val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])
configurations["functionalTestRuntimeOnly"].extendsFrom(configurations["testRuntimeOnly"])

// Add a task to run the functional tests
val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
    useJUnitPlatform()

    val projectName = this.project.name
    val taskName = this.name

    doLast {
        logger.lifecycle("Has run 'functionalTest' tasks for project: ${projectName}, with task name: $taskName")
    }
}

gradlePlugin.testSourceSets.add(functionalTestSourceSet)

tasks.named<Task>("check") {
    // Run the functional tests as part of `check`
    dependsOn(functionalTest)

    val projectName = this.project.name
    val taskName = this.name

    doLast {
        logger.lifecycle("Has run 'check' tasks for project: ${projectName}, with task name: $taskName")
    }
}

tasks.test {
    useJUnitPlatform()

    val projectName = this.project.name
    val taskName = this.name

    doLast {
        logger.lifecycle("Has run 'test' tasks for project: ${projectName}, with task name: $taskName")
    }
}


