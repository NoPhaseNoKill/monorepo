rootProject.name = "integraboost"

pluginManagement {
    repositories.gradlePluginPortal()
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositories.mavenCentral()
}

include(":modules:applications:app1")
project(":modules:applications:app1").projectDir = file("modules/applications/app1")

include(":modules:applications:app2")
project(":modules:applications:app2").projectDir = file("modules/applications/app2")

include(":modules:libraries:lib1")
project(":modules:libraries:lib1").projectDir = file("modules/libraries/lib1")

include(":modules:libraries:lib2")
project(":modules:libraries:lib2").projectDir = file("modules/libraries/lib2")
