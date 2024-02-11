

pluginManagement {
    repositories.gradlePluginPortal()
}

dependencyResolutionManagement {
    // explicitly gradle plugin portal because we only want to search for our convention plugins,
    // where the convention plugins delegate the dependency retrieval to the platform
    repositories.gradlePluginPortal()
    includeBuild("../platform")
}

rootProject.name = "plugins"

include("commons-plugin")
include("capability-conflict-avoidance-plugin")
include("tested-plugins")
include("kotlin-library-plugin")
include("kotlin-application-plugin")
include("dependency-analysis-project")
include("dependency-analysis-platform")
include("source-file-hashing-plugin")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")