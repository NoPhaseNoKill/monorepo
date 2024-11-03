




tasks.register("build") {
    dependsOn(":app:build")
    dependsOn(":module:build")
    dependsOn(tasks.named("gatherBuildScriptDependencies"), tasks.named("gatherProjectDependencies"))
}
