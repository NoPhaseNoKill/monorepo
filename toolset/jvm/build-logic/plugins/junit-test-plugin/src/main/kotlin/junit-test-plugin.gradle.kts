plugins {
    id("base-plugin")
}

dependencies {
    testImplementation(enforcedPlatform("org.junit:junit-bom"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

val testTask = tasks.test {
    useJUnitPlatform()

    /*
        Instead of doing this, it is recommended to do this at the junit level.
        By doing parallelism at the junit level, it is better able to leverage
        our CPU's, if the tests are written in a proper, parallel-safe way.

        This is done via the junitPropertiesCreationTask.

        See this for more details:
        https://gradle-community.slack.com/archives/CA745PZHN/p1708364853990869?thread_ts=1708207434.400929&cid=CA745PZHN

     */
    // maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)

    dependsOn(junitPropertiesCreationTask)

    doFirst {
        logger.lifecycle("Starting tests")
    }

    doLast {
        logger.lifecycle("Finishing tests")
    }
}

tasks.processTestResources {
    dependsOn(junitPropertiesCreationTask)
}

val junitPropertiesCreationTask = tasks.register("junitPropertiesCreationTask") {

    // Store variables to avoid project reference in the configuration cache
    val resourcesDir = file("src/test/resources")
    val baseJunitPath = resourcesDir.toPath().toString()
    val fullPath = "$baseJunitPath/junit-platform.properties"
    val propertiesFile = file(fullPath)

    // Declares task outputs for incremental build support
    outputs.file(propertiesFile)

    doLast {

        val createdResourceDir = resourcesDir.mkdir()
        val createdPropertyFile = propertiesFile.createNewFile()

        if(createdResourceDir) {
            logger.lifecycle("        - Created resource directory to use with junit-platform.properties. Path was: ${resourcesDir.path}.")
        }

        if(createdPropertyFile) {
            logger.lifecycle("        - Created junit-platform.properties. Path was: ${propertiesFile.path}.")
        }

        val propertiesLines = listOf(
            "junit.jupiter.execution.parallel.enabled" to "true",
            "junit.jupiter.execution.parallel.config.strategy" to "dynamic",
            "junit.jupiter.execution.parallel.mode.default" to "concurrent",
            "junit.jupiter.execution.parallel.mode.classes.default" to "concurrent",
        )

        val content = propertiesLines.joinToString(separator = "\n") { "${it.first} = ${it.second}" }
        val isNotExpectedContent = propertiesFile.readText().trim() != content

        if(isNotExpectedContent) {
            logger.lifecycle("        - Expected content in junit-platform.properties was not matched. Attempting to create file contents now.")

            // Ensure file is empty first
            propertiesFile.writeText("")

            propertiesLines.forEach { line ->
                propertiesFile.appendText("${line.first} = ${line.second}\n")
            }



            /*
                Should look like:
                    > Task :modules:modules:libraries:utilities:createJUnitPlatformPropertiesFile
                          |------> about to be executed
                            - Created resource directory to use with junit-platform.properties. Path was: /path/to/project/src/test/resources.
                            - Created junit-platform.properties. Path was: /path/to/project/src/test/resources/junit-platform.properties.
                            - Expected content in junit-platform.properties was not matched. Attempting to create file contents now.
                            - Created junit-platform.properties file with specified configurations.

                            Current configuration is:
                                junit.jupiter.execution.parallel.enabled = true
                                junit.jupiter.execution.parallel.config.strategy = dynamic
                                junit.jupiter.execution.parallel.mode.default = concurrent
                                junit.jupiter.execution.parallel.mode.classes.default = concurrent
                          |------> was executed

                          OR

                      > Task :modules:applications:app:createJUnitPlatformPropertiesFile
                          |------> about to be executed
                            Expected content in junit-platform.properties matched. No need to create file.
                          |------> was executed
             */

            val indentedConfigurations = propertiesLines.joinToString(separator = "\n    ") { "        ${it.first} = ${it.second}" }

            logger.lifecycle("""
            |        - Created junit-platform.properties file with specified configurations.
            |
            |        Current configuration is:
            |    $indentedConfigurations
            """.trimMargin())
        } else {
            logger.lifecycle("        Expected content in junit-platform.properties matched. No need to create file.")
        }

    }
}

tasks.register<Test>("testAll") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Test all Java code"

    dependsOn(testTask)
}
