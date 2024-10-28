
rootProject.name = "jvm"

pluginManagement {
    includeBuild("plugins/combinatory-plugins")
}

include(":plugins:non-meta-plugins")
include(":plugins:custom-plugins")

gradle.lifecycle.beforeProject {
    plugins.apply("base")
}
