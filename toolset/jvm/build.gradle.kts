plugins {
    id("io.github.janbarari.gradle-analytics-plugin") version "1.0.3"
}



repositories {
    gradlePluginPortal()
}

gradleAnalyticsPlugin {



    database {
        local = sqlite {
            path = "${rootDir.resolve("local-db")}"
            name = "local-name"
        }

        ci = null
    }

    trackingTasks = setOf(
        // Add your requested tasks to be analyzed, Example:
        ":build",
    )

    trackingBranches = setOf(
        // requested tasks only analyzed in the branches you add here, Example:
        "master",
    )

    outputPath = "OUTPUT_REPORT_PATH" // Optional: Default is project /build/ dir.
}
