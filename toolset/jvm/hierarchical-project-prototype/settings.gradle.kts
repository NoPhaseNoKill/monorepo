
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


    val kotlinVersion = "2.1.0-Beta1"


    buildscript {
        /*
            This is specifically required at this level to ensure we all get same version
         */
        repositories {
            mavenCentral()
            gradlePluginPortal()
            google()
        }
        dependencies {
            platform("org.jetbrains.kotlin:kotlin-bom:${kotlinVersion}")
        }
    }
    includeBuild("plugins/binary-root-settings-plugins")
    includeBuild("plugins/conventions-root-settings-plugins")
    includeBuild("plugins/kotlin-plugins/kotlin-plugin-one")
}

includeBuild("plugins/binary-plugins")
includeBuild("plugins/conventions-plugins")
includeBuild("plugins/included-build-plugins-that-apply-binary-plugins")
includeBuild("plugins/included-build-standalone-plugin-that-applies-binary-plugins")

includePluginProject("plugin-one-that-applies-binary-plugin", PluginProjectType.PLUGINS_THAT_APPLY_BINARY_PLUGINS)
includePluginProject("plugin-two-that-applies-binary-plugin", PluginProjectType.PLUGINS_THAT_APPLY_BINARY_PLUGINS)

includeStandaloneProject("application-one", StandaloneProjectType.APP)
includeStandaloneProject("application-two", StandaloneProjectType.APP)

includeStandaloneProject("library-one", StandaloneProjectType.LIB)
includeStandaloneProject("library-two", StandaloneProjectType.LIB)


enum class PluginProjectType(val path: String) {
    PLUGINS_THAT_APPLY_BINARY_PLUGINS("plugins-that-apply-binary-plugins"),
}

fun includePluginProject(projectName: String, type: PluginProjectType) {
    include(":${projectName}")
    project(":${projectName}").projectDir = file(File("plugins/${type.path}/${projectName}"))
}


enum class StandaloneProjectType(val path: String) {
    LIB("libraries"),
    APP("applications")
}

fun includeStandaloneProject(projectName: String, type: StandaloneProjectType) {
    include(":${projectName}")
    project(":${projectName}").projectDir = file(File("standalone-projects/${type.path}/${projectName}"))
}

rootProject.name = "hierarchical-project-prototype"

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

plugins {
    id("com.nophasenokill.binary-root-settings-plugins.binary-root-settings-plugin-one")
    id("com.nophasenokill.conventions-root-settings-plugins-plugin-one")
}
