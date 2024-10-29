rootProject.name = "plugins"

include("greeting-plugin")
include("secondary-plugin")

gradle.lifecycle.beforeProject {
    tasks.register("buildAll") {
        dependsOn(":greeting-plugin:build")
        dependsOn(":secondary-plugin:build")
    }
}
