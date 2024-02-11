plugins {
    id("base-plugin")
}

val testTask = tasks.test {
    useJUnitPlatform()
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)

    doFirst {
        logger.lifecycle("Starting tests")
    }

    doLast {
        logger.lifecycle("Finishing tests")
    }
}

tasks.register<Test>("testAll") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Test all Java code"

    dependsOn(testTask)
}
