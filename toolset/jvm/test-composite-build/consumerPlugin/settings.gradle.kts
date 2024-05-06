
rootProject.name = "consumerPlugin"

includeBuild("../producerPlugin")

dependencyResolutionManagement {
    repositories.mavenCentral()
    repositories.gradlePluginPortal()
}