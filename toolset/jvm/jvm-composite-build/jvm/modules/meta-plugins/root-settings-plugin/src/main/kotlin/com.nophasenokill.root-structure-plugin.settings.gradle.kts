enum class ApplicationName(
    override val path: String
): CustomProject {
    APPLICATION_ONE("application-one");

    override val category = ProjectCategory.APPLICATIONS
}

enum class LibraryName(override val path: String):
    CustomProject {
    LIBRARY_ONE("library-one");

    override val category = ProjectCategory.LIBRARIES
}

enum class DependencyName(override val path: String):
    CustomProject {
    BASE_DEPENDENCIES("base-dependencies");

    override val category = ProjectCategory.DEPENDENCIES
}

enum class PluginName(override val path: String):
    CustomProject {
    JAVA_BASE_PLUGIN("java-base-plugin"),
    KOTLIN_BASE_PLUGIN("kotlin-base-plugin"),
    REPOSITORIES_PLUGIN("repositories-plugin");

    override val category = ProjectCategory.PLUGINS
}

interface CustomProject {
    val path: String
    val category: ProjectCategory
}

enum class ProjectCategory(val path: String) {
    APPLICATIONS("applications"),
    LIBRARIES("libraries"),
    DEPENDENCIES("dependencies"),
    PLUGINS("plugins")
}

val combined: Set<CustomProject> =
    ApplicationName.values().toSet() +
    LibraryName.values().toSet() +
    DependencyName.values().toSet() +
    PluginName.values().toSet()

combined.map {
    includeProject(it)
}

fun includeProject(customProject: CustomProject) {

    val type: ProjectCategory = customProject.category
    val pluginName: String = customProject.path

    val projectNamePrefix = type.path.replace("${File.separatorChar}", ":")

    include(":${projectNamePrefix}:$pluginName")
    project(":${projectNamePrefix}:$pluginName").projectDir = file(File("${type.path}/${customProject.path}"))
}
