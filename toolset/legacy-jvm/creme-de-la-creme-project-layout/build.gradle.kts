
tasks.register("buildAll") {
    dependsOn(gradle.includedBuilds.map { it.task(":build") })
    dependsOn(subprojects.map { it.tasks.named("build") })
}
