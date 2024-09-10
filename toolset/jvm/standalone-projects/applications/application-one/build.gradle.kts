plugins {

    id("com.nophasenokill.kotlin-application-plugin")
    id("jacoco")
    id("com.nophasenokill.hashing-tasks-plugin")
}



dependencies {
    implementation(projects.libraryOne)
}

val jacocoOutputTask = tasks.register("jacocoOutputTask") {
    dependsOn(tasks.test)
    dependsOn(tasks.jacocoTestReport)

    val outputDir = layout.buildDirectory.dir("outputs")
    val jacocoOutputDir = layout.buildDirectory.dir("jacoco")
    val jacocoSessionsDir = layout.buildDirectory.dir("reports/jacoco/test/html")

    outputs.dir(outputDir)

    doLast {
        outputDir.get().asFile.mkdir()
        val firstDir = outputDir.get().dir("first-stage")
        outputDir.get().dir("first-stage").asFile.mkdir()
        firstDir.file("jacoco-output-task.txt").asFile.writeText("Completing jacocoOutputTask")

        val jacocoExec = jacocoOutputDir.get().file("test.exec")
        val jacocoSessions = jacocoSessionsDir.get().file("jacoco-sessions.html")

        jacocoExec.asFile.copyTo( firstDir.file("jacoco-output-task.exec").asFile, overwrite = true)
        jacocoSessions.asFile.copyTo(firstDir.file("sessions.html").asFile, overwrite = true)
    }
}

//TODO FIX BUG WITH CALCULATOR NOT COMING THROUGH IN PREVIOUS AFTER MULTIPLE RUNS

val dependsOnJacocoOutput = tasks.register("dependsOnJacocoOutput") {
    dependsOn(jacocoOutputTask)
    dependsOn(tasks.test)
    dependsOn(tasks.jacocoTestReport)
    mustRunAfter(jacocoOutputTask, tasks.jacocoTestReport, tasks.test)
    val inputText = layout.buildDirectory.dir("outputs/first-stage").map {file("jacoco-output-task.txt")}
    val inputExec = layout.buildDirectory.dir("outputs/first-stage").map { file("jacoco-output-task.exec") }


    inputs.files(inputText, inputExec)
    val outputDir = layout.buildDirectory.dir("outputs/second-stage")
    val sessionsList = outputDir.map { it.file("sessions-list.txt")}
    val firstStageBuildDir = layout.buildDirectory.dir("outputs/first-stage")
    val isFirstRunFilePath = "isFirstRun.txt"

    outputs.dir(outputDir)

    doLast {
        val inputSessionActual = firstStageBuildDir.get().asFile.resolve("sessions.html").readText()

        val sessions = mutableMapOf<String, String>()
        val rows = inputSessionActual.split("<tr>").drop(1)

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
                sessions[className] = classId
            }
        }

        println("RESULT IS:${sessions}")


        // val sessions = extractClassData(inputSessionActual)
        outputDir.get().asFile.mkdir()
        val text = firstStageBuildDir.get().asFile.resolve("jacoco-output-task.txt").readText()
        val sessionsFilesCurrent =  outputDir.get().dir("sessions-files-current")
        val sessionsFilesPrevious =  outputDir.get().dir("sessions-files-previous")
        //
        outputDir.get().file("depends-on-jacoco-output.txt").asFile.writeText("Gets values from jacoco dependant task. Text is: ${text}")
        firstStageBuildDir.get().asFile.resolve("jacoco-output-task.exec").copyRecursively(outputDir.get().file("depends-on-jacoco-output-exec.exec").asFile, overwrite = true)
        //
        sessionsFilesCurrent.asFile.mkdir()


        val sessionFileList = sessions.map {
            val file = sessionsFilesCurrent.file(it.key)
            file.asFile.createNewFile()
            file.asFile.writeText(it.value)

            it.key
        }

        val isFirstRunFile = outputDir.get().file(isFirstRunFilePath)
        isFirstRunFile.asFile.createNewFile()
        println("READ TEXT OF NEW FILE: ${isFirstRunFile.asFile.readText()}")
        val firstRunFile = outputDir.get().file(isFirstRunFilePath).asFile

        if(isFirstRunFile.asFile.readText().isEmpty()) {
            println("Is first run")
            firstRunFile.createNewFile()
            firstRunFile.writeText("true")
        } else {
            firstRunFile.writeText("false")
        }

        sessionsList.get().asFile.createNewFile()

        val sessionsString = sessionFileList.joinToString("\n") { sessionItem ->
            sessionItem
        }

        sessionsList.get().asFile.writeText(sessionsString)


        /*
            by this stage the file has been created, and has contents of either true or false
            first run == "true" -> create empty dir
            not first run == "false" -> do nothing
         */
        if(isFirstRunFile.asFile.readText() == "true") {
            println("is first run")
            sessionsFilesPrevious.asFile.mkdir()
        }
    }
}

// val combinesJacocoInfo = tasks.register("combinesJacocoInfo") {
//     dependsOn(tasks.test)
//     dependsOn(jacocoOutputTask)
//     dependsOn(dependsOnJacocoOutput)
//     dependsOn(tasks.jacocoTestReport)
//
//     val currentToCompare = layout.buildDirectory.dir("outputs/second-stage/sessions-files-current").get()
//     val previousToCompare = layout.buildDirectory.dir("outputs/second-stage/sessions-files-previous").get()
//     val difference = layout.buildDirectory.dir("outputs/third-stage")
//     val differenceOutput = difference.get().asFile.resolve("total-differences.txt")
//
//     inputs.dir(currentToCompare)
//     inputs.dir( previousToCompare )
//     outputs.file(differenceOutput)
//
//     doLast {
//
//         val currentToCompareFileTrees = currentToCompare.asFileTree
//         val previousToCompareFileTrees = previousToCompare.asFileTree
//
//
//         val currentSetFiles = currentToCompareFileTrees.files.toSet()
//         val previousSetFiles = previousToCompareFileTrees.files.toSet()
//         val diff = currentSetFiles.filter { currentFile ->
//             val found = previousSetFiles.find { return@find it.name == currentFile.name && it.readText() == currentFile.readText() }
//             return@filter found?.exists() != true
//         }
//
//         var tally = 0
//         difference.get().asFile.delete()
//
//         diff.forEach {
//             difference.get().asFile.createNewFile()
//             val output = difference.get().asFile.resolve(it.name)
//             output.writeText(it.readText())
//             tally += 1
//         }
//
//         differenceOutput.writeText("Differences found: $tally")
//     }
// }

tasks.check {
//     // dependsOn(combinesJacocoInfo)
//     dependsOn(shouldTestsRun)
//     // dependsOn(dependsOnJacocoOutput)
    dependsOn(jacocoOutputTask)
    dependsOn(dependsOnJacocoOutput)
}
//
// tasks.build {
//     // dependsOn(combinesJacocoInfo)
//     dependsOn(shouldTestsRun)
//     // dependsOn(dependsOnJacocoOutput)
//     // dependsOn(jacocoOutputTask)
// }
//
// tasks.test {
//     mustRunAfter(shouldTestsRun)
//     dependsOn(shouldTestsRun)
//     // dependsOn(combinesJacocoInfo)
//     // dependsOn(dependsOnJacocoOutput)
//     // dependsOn(jacocoOutputTask)
// }
//
// val shouldTestsRun = tasks.register("shouldTestsRun") {
//     dependsOn(combinesJacocoInfo)
//     dependsOn(tasks.test)
//     dependsOn(jacocoOutputTask)
//     dependsOn(dependsOnJacocoOutput)
//
//     val inputText = layout.buildDirectory.dir("outputs/third-stage").get().file("total-differences.txt")
//     val output = layout.buildDirectory.dir("outputs/fourth-stage").get().file("test-summary-differences.txt")
//
//     inputs.dir(layout.buildDirectory.dir("outputs/third-stage"))
//     outputs.files(output)
//
//     this.outputs.upToDateWhen {
//         inputText.asFile.readText() == "Differences found: 0"
//     }
//
//     doLast {
//         if (!inputText.asFile.exists()) {
//             println("total-differences.txt not found")
//         } else {
//             println("Differences found: ${inputText.asFile.readText() != "Differences found: 0"}")
//             output.asFile.createNewFile()
//             output.asFile.writeText(inputText.asFile.readText())
//         }
//     }
//
// }

private fun extractClassData(html: String): MutableMap<String, String> {
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
