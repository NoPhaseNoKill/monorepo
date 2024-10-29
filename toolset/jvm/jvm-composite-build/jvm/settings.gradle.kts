
rootProject.name = "jvm"

// pluginManagement {
//     includeBuild("plugins/combinatory-plugins")
//     includeBuild("plugins/non-meta-plugins")
//     includeBuild("plugins/custom-plugins")
// }

includeBuild("plugins/combinatory-plugins")
includeBuild("plugins/non-meta-plugins")
includeBuild("plugins/custom-plugins")

gradle.lifecycle.beforeProject {
    plugins.apply("base")
}
