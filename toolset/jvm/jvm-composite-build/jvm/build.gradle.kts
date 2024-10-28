

tasks.register("buildAll") {
    gradle.includedBuilds.map { it.task(":build")}
    gradle.allprojects { tasks.named("build") }
}

tasks.register("cleanAll") {
    gradle.includedBuilds.map { it.task(":clean")}
    gradle.allprojects { tasks.named("clean") }
}
