tasks.register("build") {
    dependsOn(gradle.includedBuilds.map { it.task(":build") })
    dependsOn(subprojects.map { it.tasks.named("build") })
}
