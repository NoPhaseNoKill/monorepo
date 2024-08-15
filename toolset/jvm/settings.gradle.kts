
rootProject.name = "jvm"

pluginManagement {

    val kotlinVersion = "2.0.0"
    val androidAppVersion = "8.7.0-alpha05"
    val composeVersion = "1.7.0-alpha02"

    buildscript {
        repositories {
            maven {
                url = uri("https://plugins.gradle.org/m2/")
            }
        }
        dependencies {
            classpath("org.jetbrains.kotlin:kotlin-serialization:${kotlinVersion}")
            classpath("org.jetbrains.kotlin:compose-compiler-gradle-plugin:${kotlinVersion}")
            classpath("com.android.application:com.android.application.gradle.plugin:${androidAppVersion}")
            classpath("org.jetbrains.compose:compose-gradle-plugin:${composeVersion}")
        }
    }

    repositories {

        /*
            This ensures absolute consistency across the composite build.
            Without this, we run into race conditions where included builds
            compete against each other and may load different versions.
            This should be the only version declared this way,
            the rest should be in the .toml file
         */

        plugins {
            id("org.gradle.kotlin.kotlin-dsl") version kotlinVersion
        }

        gradlePluginPortal()
        google()
        mavenCentral()
    }


    /*
                    You need to be VERY careful when changing this.

                    There are VERY subtle nuances, particularly where instrumentation is happening/the logs are non-obvious.

                    The below code:
                        1. Walks the project, and includes any subprojects or builds which are inside of it
                        2. Excludes buildSrc and root
                        3. Flattens the project name to prevent gradle from causing a bug where nested project structure
                         gets included multiple times, even though the docs at:https://docs.gradle.org/8.9/userguide/multi_project_builds.html#sub:modifying_element_of_the_project_tree
                         say it should still have worked.

                    Easiest way to confirm if you've broken something is by doing:

                    Looking at the OLD broken expectations:


                        ├── subs // Gradle would see this as a subproject
                        │   └── web // Gradle would see this as a subproject
                        │       └── my-web-module // Intended subproject
                        │           ...
                        │           └── build.gradle


                    Ensuring we don't get these in the new run when running './gradlew projects' command:


                        Root project 'jvm'
                        +--- Project ':standalone-projects-applications-application-one'
                        +--- Project ':standalone-projects-applications-application-two'
                        +--- Project ':standalone-projects-applications-application-with-instrumentation'
                        +--- Project ':standalone-projects-applications-build-tool-ui'
                        +--- Project ':standalone-projects-applications-example-desktop-application'
                        +--- Project ':standalone-projects-applications-example-package-name-relocation-app'
                        +--- Project ':standalone-projects-libraries-example-library-three'
                        +--- Project ':standalone-projects-libraries-ksp-processor'
                        +--- Project ':standalone-projects-libraries-library-one'
                        +--- Project ':standalone-projects-libraries-library-two'
                        \--- Project ':standalone-projects-libraries-some-new-lib'

                        Included builds:

                        +--- Included build ':build-logic'
                        \--- Included build ':build-logic-meta'
                 */

    includeBuild("../build-logic")
    includeBuild("../build-logic-meta")

}
/*
    DO not use dependency management here. If you need to add more repositories,
    add them to: com.nophasenokill.component-plugin.settings.gradle.kts
 */

plugins {
    id("com.nophasenokill.component-plugin")
}

settings.enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
settings.enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")


includeProject("application-one", ProjectType.APP)
includeProject("application-two", ProjectType.APP)
includeProject("application-with-instrumentation", ProjectType.APP)
includeProject("build-tool-ui", ProjectType.APP)
includeProject("example-desktop-application", ProjectType.APP)
includeProject("example-package-name-relocation-app", ProjectType.APP)
includeProject("example-library-three", ProjectType.LIB)
includeProject("ksp-processor", ProjectType.LIB)
includeProject("library-one", ProjectType.LIB)
includeProject("library-two", ProjectType.LIB)
includeProject("some-new-lib", ProjectType.LIB)

enum class ProjectType(val path: String) {
    LIB("libraries"),
    APP("applications")
}

fun includeProject(projectName: String, type: ProjectType) {
    include(":${projectName}")
    project(":${projectName}").projectDir = file(File("standalone-projects/${type.path}/${projectName}"))
}
