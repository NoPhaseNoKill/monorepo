package com.nophasenokill.tasks

//TODO come back to this later

//
// tasks.withType(Jar::class.java).configureEach {
//     manifest = java.manifest {
//         manifestContentCharset = "UTF-8"
//         isZip64 = true
//         attributes(
//             "Premain-Class" to "com.nophasenokill.TestExecutionTrackerAgent"
//         )
//     }
//
//     val file = layout.buildDirectory.file("byte-buddy-manifest.MF")
//
//     doLast {
//         manifest.writeTo(file.get().asFile)
//     }
// }
//
// val intermediaryJvmArgs = tasks.register("intermediaryJvmArgs") {
//     val agentJar = tasks.jar.get().archiveFile.get().asFile.absolutePath
//     val testClassesDir = sourceSets["test"].output.classesDirs.asFileTree.files
//     val testClassPaths = testClassesDir.joinToString(separator = File.pathSeparator) { it.absolutePath }
//
//     val baseJvmArgs = listOf("-Xmx2g", "-XX:MaxMetaspaceSize=384m","-XX:+HeapDumpOnOutOfMemoryError")
//     val additionalArgs = listOf(
//         "-javaagent:$agentJar",
//         /*
//             --add-opens=java.base/java.lang=ALL-UNNAMED
//             used to fix: https://github.com/gradle/gradle/issues/18647
//          */
//         "--add-opens=java.base/java.lang=ALL-UNNAMED",
//         "-Dtest.classpath=$testClassPaths",
//         "-Dfile.encoding=UTF-8",
//         "-Djdk.instrument.traceUsage",
//         "-ea"
//     )
//     // Ensures same order and unique args only
//     val intermediaryArgs = (baseJvmArgs + additionalArgs).toSet().sorted()
//     val outputFile = project.layout.buildDirectory.file("intermediaryJvmArgs.txt")
//
//     doFirst {
//
//         println("Output file intermediaryJvmArgs.txt: $outputFile")
//
//         if(!outputFile.get().asFile.exists()) {
//             outputFile.get().asFile.createNewFile()
//         }
//
//         outputFile.get().asFile.writeText("")
//
//         intermediaryArgs.forEach {
//             outputFile.get().asFile.appendText(it + "\n")
//         }
//
//         println("Output file intermediaryJvmArgs.txt from task intermediaryJvmArgs: ${outputFile.get().asFile.absolutePath}")
//     }
// }
//
// val forkedJvmTasks = tasks.register("forkedJvmTasks") {
//     val javaExecTasks = tasks.withType(JavaExec::class.java)
//     val testTasks = tasks.withType(Test::class.java)
//     val forkedJvmTasksOutput = project.layout.buildDirectory.file("forkedJvmTasksOutput.txt")
//     dependsOn(javaExecTasks, testTasks)
//
//     doFirst {
//         if(!forkedJvmTasksOutput.get().asFile.exists()) {
//             forkedJvmTasksOutput.get().asFile.createNewFile()
//         }
//
//         forkedJvmTasksOutput.get().asFile.writeText("")
//
//         val taskNames = javaExecTasks.map { it.name } + testTasks.map { it.name }
//         taskNames.forEach {
//             forkedJvmTasksOutput.get().asFile.appendText(it)
//         }
//     }
// }
//
// val configureJavaExecTasks = tasks.register("configureJavaExecTasks") {
//
//     dependsOn(forkedJvmTasks)
//
//     val forkedJvmTasks = project.layout.buildDirectory.file("forkedJvmTasksOutput.txt")
//     val intermediaryArgs = project.layout.buildDirectory.file("intermediaryJvmArgs.txt")
//     val intermediaryArgsOutput = project.layout.buildDirectory.file("intermediaryJvmArgsOutput.txt")
//
//
//     doFirst {
//         val forkedJvmTaskList = forkedJvmTasks.get().asFile.readLines()
//         val javaExecTasks = forkedJvmTaskList
//         println("forkedJvmTaskList: ${forkedJvmTaskList.map { it }} , size: ${javaExecTasks.size}")
//
//         javaExecTasks.map {
//             val task = tasks.named<ForkOptions>(it)
//
//             task.configure {
//                 dependsOn(intermediaryJvmArgs)
//
//                 val allJavaArgs: List<String> = allJvmArgs.mapNotNull { it }
//                 val all = intermediaryArgs.get().asFile.readLines() + allJavaArgs
//
//                 this.jvmArgs = all
//             }
//         }
//
//         if(!intermediaryArgsOutput.get().asFile.exists()) {
//             intermediaryArgsOutput.get().asFile.createNewFile()
//         }
//
//         intermediaryArgsOutput.get().asFile.writeText("")
//
//         javaExecTasks.map { task ->
//             intermediaryArgsOutput.get().asFile.appendText("Task: ${task} configured with java exec jvm args")
//         }
//
//         println("Output file intermediaryJvmArgs.txt from task configureJavaExecTasks: ${intermediaryArgs.get().asFile.absolutePath}")
//         println("Output file intermediaryJvmArgs.txt from task configureJavaExecTasks: ${intermediaryArgsOutput.get().asFile.absolutePath}")
//     }
// }
//
//
// tasks.withType(Test::class.java).configureEach {
//     dependsOn(configureJavaExecTasks)
//
//     useJUnitPlatform()
//
//     testLogging.events = setOf(
//         TestLogEvent.STARTED,
//         TestLogEvent.PASSED,
//         TestLogEvent.SKIPPED,
//         TestLogEvent.FAILED,
//         TestLogEvent.STANDARD_OUT,
//         TestLogEvent.STANDARD_ERROR,
//     )
//
//     println("allJvmArgs1 were: ${allJvmArgs}")
//     println("jvmArgs were: ${jvmArgs}")
//     println("jvmArgumentProviders.map { it.asArguments() } were: ${jvmArgumentProviders.map { it.asArguments() }}")
//
// }
//
// tasks.test {
//     dependsOn(intermediaryJvmArgs, configureJavaExecTasks)
// }
//
// tasks.build {
//     dependsOn(intermediaryJvmArgs, configureJavaExecTasks)
// }
//
// // tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
// //     jvmTargetValidationMode.set(org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode.ERROR)
// // }
//
// // kotlin {
// //     jvmToolchain {
// //         languageVersion.set(JavaLanguageVersion.of(21))
// //         vendor.set(JvmVendorSpec.MICROSOFT)
// //     }
// // }
// //
// // kotlin {
// //     compilerOptions {
// //         kotlinDaemonJvmArgs = listOf("-Xmx1500m" ,"-Xms500m")
// //     }
// // }
// //
// // tasks {
// //     compileKotlin {
// //         useDaemonFallbackStrategy.set(false)
// //     }
// // }
// //
// // tasks.withType<CompileUsingKotlinDaemon>().configureEach {
// //     compilerExecutionStrategy.set(KotlinCompilerExecutionStrategy.DAEMON)
// //     kotlinDaemonJvmArguments.set(listOf("-Xmx1500m" ,"-Xms500m"))
// // }
//
//
//
//
//
