pluginManagement {
    includeBuild("build-logic/settings")
    includeBuild("build-logic/plugins")

    // include(":base-plugin")
    // project(":base-plugin").projectDir = file("build-logic/plugins/base-plugin")
    //
    // include(":my-kotlin-plugin")
    // project(":my-kotlin-plugin").projectDir = file("build-logic/plugins/my-kotlin-plugin")
    //
    //
    // include(":library-plugin")
    // project(":library-plugin").projectDir = file("build-logic/plugins/library-plugin")
    // include(":application-plugin")
    // project(":application-plugin").projectDir = file("build-logic/plugins/application-plugin")
    //
    // include(":junit-test-plugin")
    // project(":junit-test-plugin").projectDir = file("build-logic/plugins/junit-test-plugin")
}

dependencyResolutionManagement {
    repositories.gradlePluginPortal()

    // See: https://docs.gradle.org/current/userguide/declaring_repositories.html#ex-enforcing-settings-repositories
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
}

plugins {
    id("jvm-root-settings")
}

includeBuild("build-logic/platform")
include(":modules:libraries:list")
include(":modules:libraries:utilities")
include(":modules:applications:app")