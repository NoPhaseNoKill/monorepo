pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    includeBuild("build-logic/settings")
    includeBuild("build-logic/plugins")
}

plugins {
    id("kotlin-project-root-settings")
}

rootProject.name = "jvm"

// Dynamically includes top level directories within each of the modules' sub-folders
val directories = setOf("applications", "libraries")
directories.forEach { dir ->
    rootDir
        .resolve("modules/${dir}")
        .listFiles { file -> file.isDirectory && !file.isHidden }
        ?.forEach {
            include("modules:$dir:${it.name}")
    }
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

buildCache {
    local {
        isEnabled = true
        isPush = true
    }
}
