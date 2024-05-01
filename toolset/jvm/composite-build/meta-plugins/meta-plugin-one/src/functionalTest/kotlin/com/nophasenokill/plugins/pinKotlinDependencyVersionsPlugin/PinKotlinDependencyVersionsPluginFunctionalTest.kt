package com.nophasenokill.plugins.pinKotlinDependencyVersionsPlugin

import com.nophasenokill.setup.junit.extensions.SharedTestSuiteStore
import com.nophasenokill.setup.variations.FunctionalTest
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.runTest
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import java.io.File

class PinKotlinDependencyVersionsPluginFunctionalTest : FunctionalTest() {

    @Test
    fun `should patch the kotlin kotlinBuildToolsApiClasspath and kotlinKlibCommonizerClasspath configurations to the kotlin bom version and coroutines bom version` (context: ExtensionContext) = runTest {

        val details = SharedTestSuiteStore.getSharedGradleRunnerDetails(context)

        val settingsFile = details.settingsFile
        val buildFile = details.buildFile
        val projectDir = details.projectDir


        settingsFile.writeText("""
            rootProject.name = "some-name"
            includeBuild("platforms")
            includeBuild("meta-plugins")
        """.trimIndent())

        buildFile.writeText("""
            
            import org.gradle.api.Plugin
            import org.gradle.api.Project
            import org.gradle.api.artifacts.Dependency
            import org.gradle.api.artifacts.ExternalModuleDependency
            import org.gradle.api.internal.artifacts.DefaultModuleIdentifier
            import org.gradle.api.internal.artifacts.dependencies.DefaultMinimalDependency
            import org.gradle.api.internal.artifacts.dependencies.DefaultMutableVersionConstraint
            import org.gradle.api.plugins.JavaApplication
            
            plugins {
               `java-gradle-plugin`
                id("org.jetbrains.kotlin.jvm") version "1.9.21"
            }
            
            dependencies {
                implementation(platform("com.nophasenokill.platforms:generalised-platform"))
                implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
                implementation(platform("org.junit:junit-bom"))
                implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom"))
                
                implementation("com.nophasenokill.meta-plugins:meta-plugin-one")
                implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin")

                implementation("org.jetbrains.kotlin:kotlin-stdlib")
                testImplementation("commons-io:commons-io")

                testImplementation(gradleTestKit())
            }
            
            class KotlinApplicationPlugin: Plugin<Project> {
                override fun apply(project: Project) {
                    project.pluginManager.apply("com.nophasenokill.meta-plugins.pin-kotlin-dependency-versions-plugin")
                    project.pluginManager.apply("org.jetbrains.kotlin.jvm")
                    project.pluginManager.apply("application")
                    project.pluginManager.apply("com.nophasenokill.meta-plugins.check-kotlin-build-service-fix-plugin")


                    project.extensions.getByType(JavaApplication::class.java).mainClass.set("com.nophasenokill.App")

                    project.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {

                        project.addMetaPluginDependency("meta-plugin-one")
                        project.addPlatformDependency("implementation", "com.nophasenokill.platforms", "generalised-platform")

                        project.dependencies.add("implementation", "org.slf4j:slf4j-api").apply {
                            if (this is ExternalModuleDependency) {
                                val slf4jExclusion = mapOf(
                                    "group" to "org.slf4j",
                                    "module" to "slf4j-simple"
                                )

                                this.exclude(
                                    slf4jExclusion
                                )
                            }
                        }

                        project.dependencies.add("runtimeOnly", "org.slf4j:slf4j-simple")
                        project.dependencies.add("testRuntimeOnly", "org.slf4j:slf4j-simple")


                        project.repositories.gradlePluginPortal()
                    }
                }

                private fun Project.addMetaPluginDependency(plugin: String) {
                    val moduleId = DefaultModuleIdentifier.newId("com.nophasenokill.meta-plugins", plugin)
                    val versionConstraint = DefaultMutableVersionConstraint("")
                    val dependency: Dependency = DefaultMinimalDependency(moduleId, versionConstraint)

                    project.configurations.findByName("implementation")?.dependencies?.add(dependency)
                }


                private fun Project.addPlatformDependency(configuration: String, group: String, name: String) {
                    val moduleId = DefaultModuleIdentifier.newId(group, name)
                    val versionConstraint = DefaultMutableVersionConstraint("")
                    val dependency: Dependency = DefaultMinimalDependency(moduleId, versionConstraint)

                    project.configurations.findByName(configuration)?.dependencies?.add(project.dependencies.platform(dependency))
                }
            }
            
            plugins.apply(KotlinApplicationPlugin::class.java)
        """.trimIndent())


            createMetaPluginsIncludedBuild(projectDir)
            createPlatformsIncludedBuild(projectDir)



        /*
            The contents of this file don't include shifting behaviour.

            Meaning: These lines should never change unless a configuration is updated or modified in a way
            that affects dependencies

            Note: These are effectively the same thing - it's just now picking the version from the constraint.

                Using no platform example:

                    \--- org.slf4j:slf4j-api:2.0.12

                Using platform example:

                    +--- com.nophasenokill.platforms:generalised-platform -> project :platforms:generalised-platform
                    |    \--- org.slf4j:slf4j-api:2.0.12 (c)
                    \--- org.slf4j:slf4j-api -> 2.0.12


            To verify this: You can run ./gradlew :applications:application-one:dependencyInsight --configuration testCompileClasspath --dependency slf4j --scan

                > Task :applications:application-one:dependencyInsight
                org.slf4j:slf4j-api:2.0.12 (by constraint) <------------------------****THIS IS THE IMPORTANT PART****
                  Variant compile:
                    | Attribute Name                     | Provided | Requested    |
                    |------------------------------------|----------|--------------|
                    | org.gradle.status                  | release  |              |
                    | org.gradle.category                | library  | library      |
                    | org.gradle.libraryelements         | jar      | classes      |
                    | org.gradle.usage                   | java-api | java-api     |
                    | org.gradle.dependency.bundling     |          | external     |
                    | org.gradle.jvm.environment         |          | standard-jvm |
                    | org.gradle.jvm.version             |          | 21           |
                    | org.jetbrains.kotlin.platform.type |          | jvm          |


         */


        /*
            Current versions this is applying to is for configurations
            1. kotlinBuildToolsApiClasspath and
            2. kotlinKlibCommonizerClasspath

            It fixes:

                - org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm 1.5.0 -> 1.8.0
                - org.jetbrains.kotlin:kotlin-reflect 1.6.0 -> 1.9.21

            Remaining dependency we can't touch is: kotlinCompilerClasspath which appears to have a direct
            need for reflect 1.6.10

                kotlinCompilerClasspath
                \--- org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.23
                 +--- org.jetbrains.kotlin:kotlin-stdlib:1.9.23
                 |    \--- org.jetbrains:annotations:13.0
                 +--- org.jetbrains.kotlin:kotlin-script-runtime:1.9.23
                 +--- org.jetbrains.kotlin:kotlin-reflect:1.6.10
         */

            val dependenciesResult = runExpectedSuccessTask(details, "dependencies")
            val file = getResourceFile("dependencies/kotlin-application-expected-dependencies.txt")
            val expectedContent = file.readText().lines()
            val comparableLines = getComparableBuildResultLines(dependenciesResult, 10, 10)
            val outcome = getTaskOutcome(":dependencies", dependenciesResult)
            Assertions.assertEquals(outcome, TaskOutcome.SUCCESS)
            Assertions.assertLinesMatch(expectedContent, comparableLines)
    }

    private suspend fun createMetaPluginsIncludedBuild(projectDir: File) = coroutineScope {
        val metaPluginsDir = File(projectDir.path).resolve("meta-plugins")
        metaPluginsDir.mkdirs()
        val metaPluginsSettingsFile = File(metaPluginsDir.path + "/settings.gradle.kts")

        metaPluginsSettingsFile.writeText("""
            rootProject.name = "meta-plugins"

            pluginManagement {
                repositories.gradlePluginPortal()
            }

            dependencyResolutionManagement {
                repositories.gradlePluginPortal()
            }

            include("meta-plugin-one")

            enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
            enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
        """.trimIndent())

        val metaPluginOneDir = File(projectDir.path).resolve("meta-plugins/meta-plugin-one")
        metaPluginOneDir.mkdirs()

        val metaPluginOneBuildFile = File(metaPluginOneDir.path + "/build.gradle.kts")
        metaPluginOneBuildFile.createNewFile()

        metaPluginOneBuildFile.writeText("""
            import org.gradle.api.tasks.testing.logging.TestExceptionFormat
            import org.gradle.api.tasks.testing.logging.TestLogEvent
            
            plugins {
                `java-gradle-plugin`
                id("org.jetbrains.kotlin.jvm") version "1.9.21"
            }
            
            
            group = "com.nophasenokill.meta-plugins"
            version = "0.1.local-dev"
            
            repositories {
                gradlePluginPortal()
            }
            
            gradlePlugin {
                val checkKotlinBuildServiceFixPlugin by plugins.creating {
                    id = "com.nophasenokill.meta-plugins.check-kotlin-build-service-fix-plugin"
                    implementationClass = "com.nophasenokill.CheckKotlinBuildServiceFixPlugin"
                }
            
                val kotlinMetaPlugin by plugins.creating {
                    id = "com.nophasenokill.meta-plugins.pin-kotlin-dependency-versions-plugin"
                    implementationClass = "com.nophasenokill.PinKotlinDependencyVersionsPlugin"
                }
            }
            
            /*
                 Fixes undeclared build service usage when using: enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
                 Known issue to be fixed here: https://youtrack.jetbrains.com/issue/KT-63165
            
                 Note: Because of the structure of the whole root jvm project, this ALSO needs to be applied directly
                 to this level too, as well as exposing a re-usable plugin to fix the SAME issue for other included builds
                 or projects.
             */
            
            gradle.taskGraph.whenReady {
                val allTasks = gradle.taskGraph.allTasks
                allTasks.forEach {
                    gradle.sharedServices.registrations.all {
                        val buildServiceProvider = this.service
                        val buildService = buildServiceProvider.get()
            
                        val kotlinCollectorSearchString = "org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnosticsCollector"
                        val isCollectorService = buildService.toString().contains(kotlinCollectorSearchString)
            
                        if (isCollectorService) {
                            project.logger.debug(
                                "Applying checkKotlinGradlePluginConfigurationErrors workaround to task: {} for project: {}",
                                it,
                                project.name
                            )
                            it.usesService(buildServiceProvider)
                        }
                    }
                }
            }
            
            dependencies {
                implementation(platform("com.nophasenokill.platforms:generalised-platform"))
                implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
                implementation(platform("org.junit:junit-bom"))
                implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom"))
            
                implementation("org.jetbrains.kotlin:kotlin-reflect")
                implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin")
                implementation("org.jetbrains.kotlin:kotlin-stdlib")
            
                testImplementation("commons-io:commons-io")
            
                testImplementation(gradleTestKit())
            }
            
            testing {
                suites {
            
                    val test by getting(JvmTestSuite::class) {
                        useJUnitJupiter("5.10.1")
            
                        this.targets.configureEach {
                            this.testTask.configure {
            
                                this.testLogging {
            
                                    // Log events we care about
                                    events = setOf(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
                                    displayGranularity = 1
            
                                    // Log everything
                                    info {
                                        events = setOf(TestLogEvent.STANDARD_ERROR, TestLogEvent.STARTED,TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.STANDARD_OUT, TestLogEvent.PASSED)
                                        exceptionFormat = TestExceptionFormat.FULL
                                        displayGranularity = 2
                                    }
            
                                    // Log everything
                                    debug {
                                        events = setOf(TestLogEvent.STANDARD_ERROR, TestLogEvent.STARTED,TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.STANDARD_OUT, TestLogEvent.PASSED)
                                        exceptionFormat = TestExceptionFormat.FULL
                                        displayGranularity = 2
                                    }
                                }
                            }
                            dependencies {
                                implementation(platform("com.nophasenokill.platforms:generalised-platform"))
                                implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
                                implementation(platform("org.junit:junit-bom"))
                                implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom"))
            
                                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
                                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
                                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
                            }
                        }
                    }
            
                    val functionalTest by registering(JvmTestSuite::class) {
                        useJUnitJupiter("5.10.1")
            
                        /*
                           Without this, the top-level test report aggregation will not work, possibly a bug.
                         */
                        this.testType = TestSuiteType.FUNCTIONAL_TEST
            
                        this.targets.configureEach {
                            this.testTask.configure {
            
                                this.testLogging {
            
                                    // Log events we care about
                                    events = setOf(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
                                    displayGranularity = 1
            
                                    // Log everything
                                    info {
                                        events = setOf(TestLogEvent.STANDARD_ERROR, TestLogEvent.STARTED,TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.STANDARD_OUT, TestLogEvent.PASSED)
                                        exceptionFormat = TestExceptionFormat.FULL
                                        displayGranularity = 2
                                    }
            
                                    // Log everything
                                    debug {
                                        events = setOf(TestLogEvent.STANDARD_ERROR, TestLogEvent.STARTED,TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.STANDARD_OUT, TestLogEvent.PASSED)
                                        exceptionFormat = TestExceptionFormat.FULL
                                        displayGranularity = 2
                                    }
                                }
                            }
                            dependencies {
                                implementation(project()) // functionalTest test suite depends on the production code in tests
                                implementation(platform("com.nophasenokill.platforms:generalised-platform"))
                                implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
                                implementation(platform("org.junit:junit-bom"))
                                implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom"))
            
                                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
                                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
                                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
                            }
                        }
                    }
                }
            }
            
            gradlePlugin.testSourceSets.add(sourceSets["functionalTest"])
            
            
            val functionalTestTask = tasks.register("functionalTestTask") {
                dependsOn(testing.suites.named("functionalTest"))
            }
            
            tasks.check {
                // Include functionalTest as part of the check which implicitly means build lifecycle
                dependsOn(functionalTestTask)
            }


        """.trimIndent())

        val pluginDirectoryPath = "src/main/kotlin/com/nophasenokill"
        val appDirectory = File(metaPluginOneDir.path).resolve(pluginDirectoryPath)
        appDirectory.mkdirs()

        val pluginFile  = File(appDirectory.path + "/CheckKotlinBuildServiceFixPlugin.kt")
        pluginFile.createNewFile()

        pluginFile.writeText("""
            package com.nophasenokill

            import org.gradle.api.Plugin
            import org.gradle.api.Project

            class CheckKotlinBuildServiceFixPlugin: Plugin<Project> {
                override fun apply(project: Project) {
                    /*
                         Fixes undeclared build service usage when using: enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
                         Known issue to be fixed here: https://youtrack.jetbrains.com/issue/KT-63165
                     */
                    project.gradle.taskGraph.whenReady {
                        project.tasks.named("checkKotlinGradlePluginConfigurationErrors").configure { task ->
                            val kotlinCollectorSearchString = "org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnosticsCollector"
                            project.gradle.sharedServices.registrations.all { buildServiceRegistration ->
                                val buildServiceProvider = buildServiceRegistration.service
                                val buildService = buildServiceProvider.get()
                                val isCollectorService = buildService.toString().contains(kotlinCollectorSearchString)

                                if(isCollectorService) {
                                    project.logger.debug(
                                        "Applying checkKotlinGradlePluginConfigurationErrors workaround to task: {} for project: {}",
                                        task,
                                        project.name
                                    )
                                    task.usesService(buildServiceProvider)
                                }
                            }
                        }
                    }
                }
            }
        """.trimIndent())

    }

    private suspend fun createPlatformsIncludedBuild(projectDir: File)  = coroutineScope {
        val platformDir = File(projectDir.path).resolve("platforms")
        platformDir.mkdirs()
        val platformSettingsFile = File(platformDir.path + "/settings.gradle.kts")


        platformSettingsFile.writeText(
            """
                rootProject.name = "platforms"

                pluginManagement {
                    repositories.gradlePluginPortal()
                }

                dependencyResolutionManagement {
                    repositories.gradlePluginPortal()
                }

                include("generalised-platform")

                enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
                enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
            """.trimIndent()
        )

        val generalisedPlatformDir = File(projectDir.path).resolve("platforms/generalised-platform")
        generalisedPlatformDir.mkdirs()


        val generalisedPlatformBuildFile = File(generalisedPlatformDir.path + "/build.gradle.kts")
        val generalisedPlatformSettingsFile = File(generalisedPlatformDir.path + "/settings.gradle.kts")
        generalisedPlatformBuildFile.createNewFile()
        generalisedPlatformSettingsFile.createNewFile()

        generalisedPlatformSettingsFile.writeText("""
            dependencyResolutionManagement {
                versionCatalogs {
                    create(defaultLibrariesExtensionName.get()) {
                        from(files("../../../gradle/libs.versions.toml"))
                    }
                }
            }
        """.trimIndent())

        generalisedPlatformBuildFile.writeText(
            """
                    plugins {
                        id("java-platform")
                    }

                    group = "com.nophasenokill.platforms"

                    dependencies {
                        constraints {
                        api("org.jetbrains.kotlin:kotlin-bom:1.9.23")
                        api("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
                        api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.23")
                        api("org.jetbrains.kotlin:kotlin-stdlib:1.9.23")
                
                        api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.8.0")
                        api("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.8.0")
                        runtime("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.8.0")
                
                        api("org.slf4j:slf4j-api:2.0.12")
                        api("org.slf4j:slf4j-simple:2.0.12")
                
                        api("org.gradle:gradle-tooling-api:8.7") {
                            because("It matches version of gradle being used at root")
                        }
                
                        api("commons-io:commons-io:2.16.0")
                
                        api("org.junit:junit-bom:5.10.1")
                        }
                    }
            """.trimIndent()
        )
    }
}
