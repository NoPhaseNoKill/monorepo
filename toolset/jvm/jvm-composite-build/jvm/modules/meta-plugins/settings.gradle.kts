rootProject.name = "meta-plugins"

pluginManagement {
    buildscript {
        configurations.all {
            resolutionStrategy {
                force("org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1")
            }
        }
    }
}

includeBuild("root-settings-plugin")
