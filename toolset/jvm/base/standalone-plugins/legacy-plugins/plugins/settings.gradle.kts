dependencyResolutionManagement {
    // explicitly gradle plugin portal because we only want to search for our convention plugins,
    // where the convention plugins delegate the dependency retrieval to the platform
    repositories.gradlePluginPortal()

    // See: https://docs.gradle.org/current/userguide/declaring_repositories.html#ex-enforcing-settings-repositories
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
}

rootProject.name = "plugins"

includeBuild("../platform")

// include("capability-conflict-avoidance-plugin")
// include("classpath-collision-detector-plugin")
// include("tested-plugins")
// include("dependency-analysis-project")
// include("dependency-analysis-platform")
// include("source-file-hashing-plugin")
// include("performance-metrics-plugin")
// include("dependency-analysis-plugin")

// include("base-plugin")
// include("my-kotlin-plugin")
// include("library-plugin")
// include("application-plugin")
// include("kotlin-app-plugin")
// include("junit-test-plugin")
// include("kotlin-lib-plugin")


enableFeaturePreview("STABLE_CONFIGURATION_CACHE")