plugins {
    id("com.integraboost.kotlin-application")
    id("org.jetbrains.kotlin.jvm")
    id("io.github.janbarari.gradle-analytics-plugin")
}


dependencies {
    implementation(project(":integraBoostLibrary"))
    implementation("io.arrow-kt:arrow-core:1.2.0")
}

gradleAnalyticsPlugin {
    enabled = true

    database {
        local = sqlite {
            path = "$projectDir/database"
            name = "random-db-test"
        }
    }

    trackingBranches = setOf("master")
    trackingTasks = setOf("clean", "test", "testAll", ":modules:integraBoostService:clean")
    trackAllBranchesEnabled = true

    outputPath="$projectDir/database"
}
