
pluginManagement {

    includeBuild("../custom-plugins")
    includeBuild("../non-meta-plugins")
    repositories {
        maven {
            val nophasenokillPluginPrefix: String? by settings
            val repoLocation = "../$nophasenokillPluginPrefix/build/repo"
            url = uri(repoLocation)
        }
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.nophasenokill.custom-plugins") {
                useModule("com.nophasenokill:custom-plugins:${requested.version}")
            }

            if (requested.id.namespace == "com.nophasenokill.non-meta-plugins") {
                useModule("com.nophasenokill:non-meta-plugins:${requested.version}")
            }
        }
    }
}

rootProject.name = "combinatory-plugins"
