rootProject.name = "jvm"

pluginManagement {
    /*
        Including these here allows us to access plugins and prevents them being unable to be
        found.
     */
    includeBuild("known-to-be-working-examples/plugins/root-settings-plugin")
    includeBuild("known-to-be-working-examples/plugins/hash-source-plugin")
    repositories {
        gradlePluginPortal() // Allows local plugins to be found
    }
}

dependencyResolutionManagement {
    rulesMode = RulesMode.FAIL_ON_PROJECT_RULES
    repositories {
        /*
            Allows us to declare dependency on the plugin at the project level

            Without this, the included project of :known-to-be-working-examples:plugins:extended-plugin, would get:

            Could not determine the dependencies of task ':known-to-be-working-examples:plugins:extended-plugin:compileJava'.
            > Could not resolve all task dependencies for configuration ':known-to-be-working-examples:plugins:extended-plugin:compileClasspath'.
               > Could not find com.nophasenokill.hash-source-plugin:hash-source-plugin:0.1.local-dev.
         */
        includeBuild("known-to-be-working-examples/plugins/hash-source-plugin")
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.nophasenokill.root-settings-plugin")
}

include("known-to-be-working-examples:plugins:basic-plugin")
include("known-to-be-working-examples:plugins:extended-plugin")
includeBuild("known-to-be-working-examples/domain")

