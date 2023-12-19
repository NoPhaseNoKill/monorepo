plugins {
    id("io.github.janbarari.gradle-analytics-plugin") version "1.0.1"
}

gradleAnalyticsPlugin {
    enabled = true

    database {
        local = sqlite {
            path = "$rootDir/database"
            name = "build-analyzer-db"
        }
    }

    trackingBranches = setOf("master")
    trackingTasks = setOf("clean", "test", "testAll")
    trackAllBranchesEnabled = true

    outputPath="$rootDir/database"
}

tasks.register("testAll") {
    group = "verification"
    description = "Run all tests after clearing the cache"
    dependsOn(gradle.includedBuild("modules").task(":integraBoostLibrary:clean"))
    dependsOn(gradle.includedBuild("modules").task(":integraBoostService:clean"))
    dependsOn(gradle.includedBuild("modules").task(":integraBoostLibrary:test"))
    dependsOn(gradle.includedBuild("modules").task(":integraBoostService:test"))
}