rootProject.name = "jvm"

includeBuild("base/standalone-plugins")
includeBuild("base/modules")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")