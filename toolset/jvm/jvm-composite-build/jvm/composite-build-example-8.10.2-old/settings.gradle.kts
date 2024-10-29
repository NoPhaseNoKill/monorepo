rootProject.name = "consumer"


includeBuild("modules/plugins")
includeBuild("modules/applications")
includeBuild("modules/libraries")

gradle.lifecycle.beforeProject {
    tasks.register("build") {
        gradle.includedBuilds.map { dependsOn(it.task(":buildAll")) }
    }
}

// To function outside a composite, a plugin repository would be required here
