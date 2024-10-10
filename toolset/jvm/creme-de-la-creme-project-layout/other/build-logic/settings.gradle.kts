rootProject.name = "build-logic"

gradle.lifecycle.beforeProject {
    apply(plugin = "base")
    println("Hello from settings v1 ${file(".")}")
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
