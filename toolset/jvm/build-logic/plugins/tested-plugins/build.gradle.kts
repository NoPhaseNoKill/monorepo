plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    id("java-gradle-plugin")

    // In future, it might be beter to alternatively use alias(libs.plugins.jvm)
    `kotlin-dsl`
}

dependencies {
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
    implementation(enforcedPlatform("org.jetbrains.kotlin:kotlin-bom"))
    testImplementation(enforcedPlatform("org.junit:junit-bom"))

    /*
        adds kotlin to the project. org.jetbrains.kotlin:kotlin-stdlib manages ensures that when we set kotlin.stdlib.default.dependency=false
        that our apps/libraries still get kotlin
        DO NOT USE:
            - implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
            - kotlin("stdlib")
     */
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // applies test projects
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

gradlePlugin {
    // Define the plugin
    val greeting by plugins.creating {
        id = "greeting-plugin"
        implementationClass = "com.nophasenokill.GreetingPlugin"
    }

    val commons by plugins.creating {
        id = "commons-tested-plugin"
        implementationClass = "com.nophasenokill.CommonsTestedPlugin"
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
}

gradlePlugin.testSourceSets.add(functionalTestSourceSet)

tasks.named<Task>("check") {
    // Run the functional tests as part of `check`
    dependsOn(functionalTest)
    doLast {
        logger.lifecycle("Has run check on the tested-plugins")
    }
}

val testTask = tasks.named<Test>("test") {
    useJUnitPlatform()

    doLast {
        logger.lifecycle("Has run test on the tested-plugins")
    }
}

tasks.register<Test>("testAll") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Runs plugin unit and functional tests"

    useJUnitPlatform()

    dependsOn(functionalTest)
    dependsOn(testTask)

    doLast {
        logger.lifecycle("Has run testAll on the tested-plugins")
    }
}