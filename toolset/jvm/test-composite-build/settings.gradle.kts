rootProject.name = "test-composite-build"

includeBuild("consumerPlugin")
includeBuild("producerPlugin")

dependencyResolutionManagement {
    repositories.mavenCentral()
    repositories.gradlePluginPortal()
}