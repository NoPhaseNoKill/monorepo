plugins {
    `java` // Apply Java plugin globally if needed
}

allprojects {
    repositories {
        mavenCentral()
    }

    // Enable build cache
    // gradle.startParameter.isBuildCacheEnabled = true
    // gradle.startParameter.isConfigurationCacheEnabled = true
}

subprojects {
    apply(plugin = "java")

    tasks.withType<JavaCompile> {
        options.isIncremental = true
    }

    // Dynamic task creation for compiling all projects
    tasks.register("compileAll") {
        dependsOn(subprojects.map { it.tasks.named("compileJava") })
    }
}
