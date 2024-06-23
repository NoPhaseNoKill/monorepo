plugins {
    id("base")
    id("test-report-aggregation")
}

dependencies {
    testReportAggregation("com.nophasenokill.applications:application-one")
}

reporting {
    reports {
        val testAggregateTestReport by creating(AggregateTestReport::class) {
            testType = TestSuiteType.UNIT_TEST
        }

        val testAggregateFunctionalTestReport by creating(AggregateTestReport::class) {
            testType = TestSuiteType.FUNCTIONAL_TEST
        }
    }
}

tasks.check {
    dependsOn(tasks.named<TestReport>("testAggregateTestReport"))
    dependsOn(tasks.named<TestReport>("testAggregateFunctionalTestReport"))
}

# Create and write to settings.gradle.kts in the root directory
cat > "settings.gradle.kts" <<EOL
        rootProject.name = "root-project"
includeFlat("oneLevelDeep")


gradle.afterProject {
    rootDir.resolve("oneLevelDeep").listFiles().forEach { file ->
        println("[${project.name}] file directory: ${file.isDirectory}")

        if (file.isDirectory()) {
            println("[${project.name}] Is file directory: ${file.isDirectory}")
            file.listFiles().forEach { subDir ->

                println("[${project.name}] Is directory subdir: ${file.isDirectory}")
                println("[${project.name}] Is directory subdir: ${file.isDirectory}")
                println()

                if (subDir.isDirectory() && !subDir.isHidden) {
                    val subDirPath = "${gradle.rootProject.layout.projectDirectory}:oneLevelDeep:${subDir.parentFile.name}:${subDir.name}"
                    val path = "${gradle.rootProject.layout.projectDirectory}:oneLevelDeep:${subDir.parentFile.name}:${subDir.name}"
                    println("Path is: ${path}")
                    println("subDirPath is: $subDirPath")
                    // include(path)
                }
            }
        }
    }
}
EOL

# Create and write to build.gradle.kts in the root directory
cat > "build.gradle.kts" <<EOL
        plugins {
            \`java\` // Apply Java plugin globally if needed
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
EOL
ensures that uploading is done as a part of the build. If you want the build to finish sooner, and
# also are not in an environment that terminates/exits before the upload can complete, set it to true.
# True value is currently being used to us an 'overall' more accurate build time