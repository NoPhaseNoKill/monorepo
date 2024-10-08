
rootProject.name = "jvm"

gradle.lifecycle.beforeProject {
    apply(plugin = "base")
    println("BUILD TREE PATH: ${project.buildTreePath}")

    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }

    val rootDir = project.isolated.rootProject.projectDirectory
    println("The root project directory is $rootDir")
}

pluginManagement {

    /*
        This ensures absolute consistency across the composite build.
        Without this, we run into race conditions where included builds
        compete against each other and may load different versions.
        This should be the only version declared this way,
        the rest should be in the .toml file
     */


    // Declared below in plugins section also
    val kotlinVersion = "2.1.0-Beta1"

    buildscript {
        dependencies {
            classpath(platform("org.jetbrains.kotlin:kotlin-bom:${kotlinVersion}"))
            classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
        }
    }


    includeBuild("../build-logic/structural-plugins")
    includeBuild("../build-logic/meta-gradle-utilities")
    includeBuild("../build-logic/meta-byte-buddy")
    includeBuild("../build-logic/kotlin-plugins")

    includeBuild("hierarchical-project-prototype/plugins/binary-root-settings-plugins")
    includeBuild("hierarchical-project-prototype/plugins/conventions-root-settings-plugins")
    includeBuild("hierarchical-project-prototype/plugins/kotlin-plugins/kotlin-plugin-one")
}

/*
    DO not use dependency management here. If you need to add more repositories,
    add them to: com.nophasenokill.component-plugin.settings.gradle.kts
 */

plugins {
    // Declared above in pluginManagement section also
    val kotlinVersion = "2.1.0-Beta1"

    kotlin("jvm") version(kotlinVersion) apply false
    `kotlin-dsl` apply false

    id("com.nophasenokill.component-plugin")
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")



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
                    +--- Project ':standalone-projects-libraries-example-library-three'
                    +--- Project ':standalone-projects-libraries-ksp-processor'
                    +--- Project ':standalone-projects-libraries-library-one'
                    +--- Project ':standalone-projects-libraries-library-two'
                    \--- Project ':standalone-projects-libraries-some-new-lib'

                    Included builds:

                    +--- Included build ':build-logic'
                    \--- Included build ':build-logic-meta'
             */

includeProject("application-one", ProjectType.APP)
includeProject("application-two", ProjectType.APP)
includeProject("example-desktop-application", ProjectType.APP)
includeProject("example-library-three", ProjectType.LIB)
includeProject("library-one", ProjectType.LIB)
includeProject("library-two", ProjectType.LIB)

includeProject("application-one", ProjectType.HIERARCHICAL_APP)
includeProject("application-two", ProjectType.HIERARCHICAL_APP)
includeProject("library-one", ProjectType.HIERARCHICAL_LIB)
includeProject("library-two", ProjectType.HIERARCHICAL_LIB)
includeProject("plugin-one-that-applies-binary-plugin", ProjectType.HIERARCHICAL_PLUGINS_THAT_APPLY_BINARY_PLUGINS)
includeProject("plugin-two-that-applies-binary-plugin", ProjectType.HIERARCHICAL_PLUGINS_THAT_APPLY_BINARY_PLUGINS)

includeBuild("${IncludedBuildType.HIERARCHICAL_STANDALONE_PLUGINS.path}/binary-plugins")
includeBuild("${IncludedBuildType.HIERARCHICAL_STANDALONE_PLUGINS.path}/conventions-plugins")
includeBuild("${IncludedBuildType.HIERARCHICAL_STANDALONE_PLUGINS.path}/included-build-plugins-that-apply-binary-plugins")
includeBuild("${IncludedBuildType.HIERARCHICAL_STANDALONE_PLUGINS.path}/included-build-standalone-plugin-that-applies-binary-plugins")

enum class IncludedBuildType(val path: String) {
    STANDALONE_PROJECTS("standalone-projects"),
    HIERARCHICAL_STANDALONE_PROJECTS("hierarchical-project-prototype/standalone-projects"),
    HIERARCHICAL_STANDALONE_PLUGINS("hierarchical-project-prototype/plugins"),
}

enum class ProjectType(val path: String) {
    LIB("${IncludedBuildType.STANDALONE_PROJECTS.path}/libraries"),
    APP("${IncludedBuildType.STANDALONE_PROJECTS.path}/applications"),
    HIERARCHICAL_LIB("${IncludedBuildType.HIERARCHICAL_STANDALONE_PROJECTS.path}/libraries"),
    HIERARCHICAL_APP("${IncludedBuildType.HIERARCHICAL_STANDALONE_PROJECTS.path}/applications"),
    HIERARCHICAL_PLUGINS_THAT_APPLY_BINARY_PLUGINS("${IncludedBuildType.HIERARCHICAL_STANDALONE_PLUGINS.path}/plugins-that-apply-binary-plugins"),
}

fun includeProject(projectName: String, type: ProjectType) {
    val projectNamePrefix = type.path.replace("${File.separatorChar}", ":")
    println("""

        Old name: ${projectName}
        New name: ${projectNamePrefix}:$projectName
        Project name prefix: ${projectNamePrefix}, type: ${type.path}, project name: ${projectName}

    """.trimIndent())
    include(":${projectNamePrefix}:$projectName")
    project(":${projectNamePrefix}:$projectName").projectDir = file(File("${type.path}/${projectName}"))
}
