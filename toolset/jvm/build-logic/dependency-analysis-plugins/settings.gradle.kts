
pluginManagement {
    repositories.gradlePluginPortal()
}

dependencyResolutionManagement {
    // explicitly gradle plugin portal because we only want to search for our convention plugins,
    // where the convention plugins delegate the dependency retrieval to the platform
    repositories.gradlePluginPortal()
    includeBuild("../platform")
}

rootProject.name = "dependency-analysis-plugins"

include("dependency-analysis-platform")
include("dependency-analysis-project")