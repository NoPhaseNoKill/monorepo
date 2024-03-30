
rootProject.name = "libraries"

include("library-one")
include("library-two")

includeBuild("../platforms")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")



