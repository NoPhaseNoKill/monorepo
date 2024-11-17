
plugins {
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    // enable all default rules
    buildUponDefaultConfig = true

    // customize some of the rules, until we can fix the offending cases
    config.convention(project.isolated.rootProject.projectDirectory.file("gradle/detekt.yml"))

    // also check the project build file
    source.from(project.buildFile)
}

pluginManager.withPlugin("com.nophasenokill.code-quality") {
    tasks {
        named("codeQuality") {
            dependsOn(detekt)
        }
    }
}

