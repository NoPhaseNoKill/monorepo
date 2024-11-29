rootProject.name = "convention-plugins"

/*
    When leveraging exclusive content filtering in the pluginManagement section
    specifically in the settings.gradle.kts file, "...it becomes illegal to add more
    repositories through the project buildscript.repositories. In that case, the build
    configuration will fail. "

    See https://docs.gradle.org/current/userguide/userguide_single.html#sec:declaring-content-repositories
    for more details.
 */

pluginManagement {

    plugins {
        id("com.gradle.develocity") version "3.18.1"
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    }

    repositories {
        exclusiveContent {
            forRepository {
                /*
                    Currently resolves everything from Gradle Plugin Portal. See below for sample if we need to change this in future
                 */
                gradlePluginPortal()
            }
            filter {
                /*
                    Include all plugins (this is the default behavior for gradlePluginPortal),
                    but allows the flexibility to add an exclusion if we decide to have plugins
                    built to a particular local repo in the future. This could then be added like:

                        repositories {
                            // This would mean the maven url would only be for com.nophasenokill artifacts
                            maven {
                                uri = file("some-local-plugin-repo")
                            }

                            exclusiveContent {
                                forRepository {
                                    gradlePluginPortal() // Only for NON com.nophasenokill plugins
                                }
                                filter {
                                    includeGroupByRegex("^(?!com\.nophasenokill).*$") // Matches all groups EXCEPT com.nophasenokill
                                }
                            }
                        }
                 */
                includeGroupByRegex(".*")
            }
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)

    repositories {
        /*
            Default repository for dependencies which will always be in the mavenCentral repo.
            This is primarily for speed, due to things like plugin binary dependencies never
            being found here. Hence we declare that things like the kotlin dsl, and
            kotlin jvm gradle plugins should never use the mavenCentral - and always
            go straight to the gradlePluginPortal() instead.
         */
        mavenCentral()


        exclusiveContent {
            forRepository {
                gradlePluginPortal() // Only for Kotlin-related plugin specific dependencies
            }
            filter {
                includeModule("org.jetbrains.kotlin", "kotlin-dsl")
                includeModule("org.jetbrains.kotlin", "kotlin-gradle-plugin")
            }
        }
    }
}

/*
    Applies to the root settings
 */
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention")
    id("com.gradle.develocity")
}



/*
    Applies to each sub project.

    **** BEWARE **** THIS DOES NOT INCLUDE THIS FOR INCLUDED BUILDS **** BEWARE ****
 */
gradle.lifecycle.beforeProject {
    project.plugins.apply("base")

    tasks.register("buildAll") {
        gradle.includedBuilds.map { dependsOn(it.task(":buildAll"))}
        subprojects.forEach {
            dependsOn(it.tasks.named("buildAll"))
        }
    }
}

develocity {
    buildScan {
        publishing.onlyIf {
            val hasFailures = it.buildResult.failures.isNotEmpty()
            println("Decision to publish build scan was: ${hasFailures}.")
            if (hasFailures) {
                println("Failures were: ${it.buildResult.failures}")
            }
            hasFailures
        }

        uploadInBackground.set(false)
        termsOfUseUrl.set("https://gradle.com/terms-of-service")
        termsOfUseAgree.set("yes")

        obfuscation {
            username { "OBFUSCATED_USERNAME" }
            ipAddresses { addresses -> addresses.map { _ -> "OBFUSCATED_HOSTNAME" } }
            hostname { "OBFUSCATED_IP_ADDRESS" }
        }
    }
}

includeProject("convention-plugin-one", ProjectType.CONVENTION_PLUGINS)

enum class ProjectType(val path: String) {
    CONVENTION_PLUGINS("convention-plugins"),
}

fun includeProject(projectName: String, type: ProjectType) {
    val projectNamePrefix = type.path.replace("${File.separatorChar}", ":")
    logger.quiet("")
    val boldedDetailsHeading = "\u001B[1mTroubleshooting details\u001B[0m"
    logger.quiet(
        """
        *** Including project final result: ':${projectNamePrefix}:$projectName' ***

            ${boldedDetailsHeading}
                [project-name-original]     '${projectName}'
                [project-type]              '${type.path}'
                [project-name-prefix]       '${projectNamePrefix}'
                [project-path]              '${type.path}/${projectName}'
                [included-name-aka-final]   ':${projectNamePrefix}:$projectName'

    """.trimIndent()
    )
    /*
        Note this is different from the main build, as it's now just the normal includes
        ie:
            include("convention-plugin-one")

        rather than:
            include("build-logic-convention-plugins-convention-plugin-one")
     */

    include(":$projectName")
    project(":$projectName").projectDir = file(File(projectName))
}
