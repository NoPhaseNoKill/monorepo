rootProject.name = "test-composite-build"
include("producerPlugin", "consumerPlugin")

dependencyResolutionManagement {
    repositories.mavenCentral()
    repositories.gradlePluginPortal()
}