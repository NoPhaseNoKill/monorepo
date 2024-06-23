
tasks.register("buildAll") {
    dependsOn(
        gradle
            .includedBuilds
            .filter { it.name !== "composite-build" }
            .map { it.task(":build") })
}