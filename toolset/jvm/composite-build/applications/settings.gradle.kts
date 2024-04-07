
rootProject.name = "applications"

include("application-one")
includeBuild("../standalone-plugins")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

