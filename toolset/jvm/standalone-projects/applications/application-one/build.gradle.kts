plugins {
    id("com.nophasenokill.kotlin-application-plugin")
}

/*
    Allows us to consume the results of our the test task from our dependencies,
    which could form an aggregation of binary test result outputs.

    TODO
    
    This could be used with jacoco, to output something similar
    to the configuration cache. We would then group by hash/folder name,
    where each project outputs whether or not we need to re-run the test task.
    We can then consume it here, aggregate them all, and make a determination
    of whether or not the affected code we have touched needs re-running (incremental
    feedback)
 */

val testReportData: Configuration by configurations.creating {
    isCanBeConsumed = false
    attributes {
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("test-report-data"))
    }
}

val testReportTask = tasks.register<TestReport>("testReportTask") {
    destinationDirectory = reporting.baseDirectory.dir("allTests")
    // Use test results from testReportData configuration
    testResults.from(testReportData)
}

tasks.build {
    dependsOn(testReportTask)
}

dependencies {
    implementation(projects.standaloneProjects.libraries.libraryOne)
    testReportData(projects.standaloneProjects.libraries.libraryOne)
}


/*
    Determines whether to re-run tests based on the hash outputs for each class hash
    that jacoco produces. This represents the test execution, and will be able to tell
    us if there is a difference in any of our test execution (which is important, because
    matching on the source files changing along doesn't guarantee we don't hit new code
    due to execution paths etc).

    Currently, this checks all files, which is semi-useless, but we now have all of the hashes
    which were the hard thing. We can now apply filtering by narrowing down even further (say with
    a file filter/some form of namespacing like 'Test').

    Plans are:
        1. Output all files and their execution hashes                            - DONE
        2. Determine based on run tests, whether or not we need to run them again - TODO
        3. It should also cater for no source file changes for the upToDate check - TODO
        4. Port this across to the JacocoPlugin file                              - TODO
 */

val shouldTestsRunTask = tasks.register("shouldTestsRunTask") {
    val jacocoTestReport = tasks.named("jacocoTestReport")
    dependsOn(jacocoTestReport)

    val exec = jacocoTestReport.get().inputs.files.asFileTree.filter {
        it.name.contains("exec")
    }
    val incrementalDir = layout.buildDirectory.dir("holy-grail-incremental-dir")

    inputs.files(exec)
    outputs.dir(incrementalDir)
    doLast {

        val sessions = getSessions()

        val current = incrementalDir.get().asFile.resolve("current.txt")
        val previous = incrementalDir.get().asFile.resolve("previous.txt")
        val outcome = incrementalDir.get().asFile.resolve("outcome.txt")
        var shouldRunTests = true

        if(sessions == null) {
            current.createNewFile()
            previous.createNewFile()
        } else if(!current.exists()) {
            current.writeText(sessions.get().readText())
            previous.createNewFile()
        } else if (previous.readText().isEmpty() && current.readText().isNotEmpty()) {
            current.copyTo(previous, overwrite = true)
            previous.writeText(sessions.get().readText())
        } else if(previous.readText().isNotEmpty()) {

            val contentsSame = sessions.get().readText() == previous.readText() && previous.readText() == current.readText()

            if(contentsSame) {
                // do nothing
                shouldRunTests = false
            } else {
                previous.copyTo(current, overwrite = true)
                previous.writeText(sessions.get().readText())
            }
        }
        outcome.writeText("Should run tests: $shouldRunTests")
    }
}

private fun getSessions(): Provider<File>? {
    val inputDir = tasks.named("jacocoTestReport").get().outputs.files.asFileTree
    val outputDir = layout.buildDirectory.dir("jacoco-session")
    val newFile = outputDir.map { it.asFile.resolve("jacoco-sessions.txt") }

    outputDir.get().asFile.mkdirs()

    val sessionFile = inputDir.files.firstOrNull { it.name == "jacoco-sessions.html" }
    if (sessionFile != null && sessionFile.exists()) {

        newFile.get().createNewFile()
        val readContents = extractClassData(sessionFile.readText())
        val allContents = readContents.map { it.toString() }
        val allContent = allContents.joinToString(separator = "\n") { content -> content }
        newFile.get().writeText(allContent) // Writes all content at once, overwriting any existing content
        return newFile
    } else {
        logger.info("Jacoco sessions file not found, skipping 'sessions' task.")
        return null
    }

}

private fun extractClassData(html: String): Map<String, String> {
    val result = mutableMapOf<String, String>()
    val rows = html.split("<tr>").drop(1)

    for (row in rows) {

        val classStart = row.indexOf(">") + 1
        val classEnd = row.indexOf("</")
        if (classStart == 0 || classEnd == -1) continue
        val classNamePart = row.substring(classStart, classEnd)
        val className = classNamePart.split(">").last().split("<").first()


        val idStart = row.indexOf("<code>") + 6
        val idEnd = row.indexOf("</code>")
        if (idStart < 6 || idEnd == -1) continue
        val classId = row.substring(idStart, idEnd)

        if (className.isNotEmpty() && classId.isNotEmpty()) {
            result[className] = classId
        }
    }
    return result
}
