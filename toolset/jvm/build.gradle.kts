

tasks.register("checkAll") {
    group = "verification"
    description = "Run all checks"

    dependsOn(gradle.includedBuild("plugin").task(":build"))
    dependsOn(":libraries:list:build")
    dependsOn(":libraries:utilities:build")
    dependsOn(":applications:app:build")
    dependsOn(":applications:accelerated-test-suite-runner:build")
}