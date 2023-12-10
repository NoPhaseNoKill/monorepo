
tasks.register("testAll") {
    group = "verification"
    description = "Run all tests after clearing the cache"
    dependsOn(gradle.includedBuild("modules").task(":integraBoostLibrary:clean"))
    dependsOn(gradle.includedBuild("modules").task(":integraBoostService:clean"))
    dependsOn(gradle.includedBuild("modules").task(":integraBoostLibrary:test"))
    dependsOn(gradle.includedBuild("modules").task(":integraBoostService:test"))
}