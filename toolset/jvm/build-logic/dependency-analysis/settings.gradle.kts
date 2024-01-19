
pluginManagement {
    repositories.gradlePluginPortal()
    includeBuild("../plugins")
}

dependencyResolutionManagement {
    // explicitly gradle plugin portal because we only want to search for our convention plugins,
    // where the convention plugins delegate the dependency retrieval to the platform
    repositories.gradlePluginPortal()
    includeBuild("../platform")

}

rootProject.name = "dependency-analysis"

include("dependency-analysis-project")