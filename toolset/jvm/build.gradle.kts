
tasks.register("check") {
    group = "verification"
    description = "Run all checks"

    dependsOn(gradle.includedBuild("standalone-plugins").task(":plugin:check"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:list:check"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:utilities:check"))
    dependsOn(gradle.includedBuild("modules").task(":applications:app:check"))
    dependsOn(gradle.includedBuild("modules").task(":applications:accelerated-test-suite-runner:check"))
}