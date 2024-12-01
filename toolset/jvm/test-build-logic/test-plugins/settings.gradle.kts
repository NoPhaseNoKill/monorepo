rootProject.name = "test-plugins"

include("test-plugin-one")

gradle.lifecycle.beforeProject {
    tasks.register("build") {
        dependsOn(gradle.includedBuilds.map { it.task(":build") })
    }
}
