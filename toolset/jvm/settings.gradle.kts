pluginManagement {
    includeBuild("build-logic/settings")
}

plugins {
    id("jvm-root-settings")
}

// include(":build-logic:plugins:base-plugin")
// project(":build-logic:plugins:base-plugin").projectDir = file("build-logic/plugins/base-plugin")
//
// include(":build-logic:plugins:my-kotlin-plugin")
// project(":build-logic:plugins:my-kotlin-plugin").projectDir = file("build-logic/plugins/my-kotlin-plugin")
//
//
// include(":build-logic:plugins:library-plugin")
// project(":build-logic:plugins:library-plugin").projectDir = file("build-logic/plugins/library-plugin")
// include(":build-logic:plugins:application-plugin")
// project(":build-logic:plugins:application-plugin").projectDir = file("build-logic/plugins/application-plugin")
//
// include(":build-logic:plugins:junit-test-plugin")
// project(":build-logic:plugins:junit-test-plugin").projectDir = file("build-logic/plugins/junit-test-plugin")

includeBuild("build-logic/plugins")
includeBuild("modules")