import java.util.*

rootProject.name = "integraboost"

pluginManagement {
    repositories.gradlePluginPortal()
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories.mavenCentral()
}

/**
 * This offers an alternative way of including and setting project directories.
 *
 * Previously you would need to do:
 *     // library example
 *     include(":modules:libraries:lib2")
 *     project(":modules:libraries:lib2").projectDir = file("modules/libraries/lib2")
 *
 *     // application example
 *     include(":modules:applications:app2")
 *     project(":modules:applications:app2").projectDir = file("modules/applications/app2")
 */
modules {
    libraries {
        subproject("lib1")
        subproject("lib2")
    }
    applications {
        subproject("app1")
        subproject("app2")
    }
}

fun modules(configuration: Modules.() -> Unit) {
    Modules().configuration()
}

enum class ModuleType {
    Libraries,
    Applications;

    @Override
    override fun toString(): String {
        return this.name.lowercase(Locale.getDefault())
    }
}

class Modules {
    fun libraries(configuration: Libraries.() -> Unit) {
        Libraries().configuration()
    }

    fun applications(configuration: Applications.() -> Unit) {
        Applications().configuration()
    }

    inner class Libraries {
        fun subproject(projectName: String) = includeProject(ModuleType.Libraries.toString(), projectName)
    }

    inner class Applications {
        fun subproject(projectName: String) = includeProject(ModuleType.Applications.toString(), projectName)
    }

    private fun includeProject(type: String, projectName: String) {
        val fullProjectPath = ":modules:$type:$projectName"
        include(fullProjectPath)
        project(fullProjectPath).projectDir = file("modules/$type/$projectName")
    }
}