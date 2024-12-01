rootProject.name = "test-build-logic"

pluginManagement {
    includeBuild("test-plugins")
}

gradle.lifecycle.beforeProject {
    tasks.register("build") {
        dependsOn(gradle.includedBuilds.map { it.task(":build") })
    }
}
