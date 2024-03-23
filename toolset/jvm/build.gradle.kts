
val pluginTask = tasks.register("pluginTask") {
    dependsOn(gradle.includedBuild("standalone-plugins").task(":plugin:build"))
    dependsOn(gradle.includedBuild("standalone-plugins").task(":plugin:assemble"))
    dependsOn(gradle.includedBuild("standalone-plugins").task(":plugin:check"))
}

tasks.register("checkAll") {
    group = "verification"
    description = "Run all checks"

    mustRunAfter(pluginTask)

    dependsOn(gradle.includedBuild("modules").task(":libraries:list:check"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:utilities:check"))
    dependsOn(gradle.includedBuild("modules").task(":applications:app:check"))
    dependsOn(gradle.includedBuild("modules").task(":applications:accelerated-test-suite-runner:check"))
}