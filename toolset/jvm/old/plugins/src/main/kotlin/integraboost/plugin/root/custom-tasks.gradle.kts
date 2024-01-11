package integraboost.plugin.root

tasks.register("buildAll") {
    group = "local"
    description = "Build all subprojects"
    subprojects.forEach { subproject ->
        if (subproject.tasks.findByName("build") != null) {
            dependsOn(subproject.tasks.named("build"))
        }
    }
}

tasks.register("cleanAll") {
    group = "local"
    description = "Clean all subprojects"
    subprojects.forEach { subproject ->
        if (subproject.tasks.findByName("clean") != null) {
            dependsOn(subproject.tasks.named("clean"))
        }
    }
}