plugins {
    id("base-plugin")
    id("application-plugin")
    id("junit-test-plugin")
    id("my-kotlin-plugin")
    // id("source-file-hashing-plugin")
    // id("capability-conflict-avoidance-plugin")
    // id("dependency-analysis-project")
    // id("commons-tested-plugin")
    // id("greeting-plugin")
}

/*
    Should display something like:
        > Task :app:printRuntimeClasspath
              |------> about to be executed

              [EVENT] Checking classpath capability conflict avoidance...

              Classpath correctly applied capability conflict avoidance

                  Contained:
                     - 'jakarta.activation/jakarta.activation-api/1.2.2'

                  Did not contain:
                     - 'javax.activation:activation:1.1.1'
                     - 'javax.activation:activation:1.1'

              |------> was executed

        > Task :utilities:compileKotlin UP-TO-DATE
 */
// val printRuntimeClasspathTask = tasks.register("printRuntimeClasspath") {
//     inputs.files(sourceSets["main"].runtimeClasspath).withPropertyName("runtimeClasspath").withPathSensitivity(PathSensitivity.ABSOLUTE)
//
//     val outputDir = layout.buildDirectory.dir("printRuntimeClasspath")
//     val outputFile = outputDir.map { it.file("runtimeClasspathCheckResult.txt") }
//     outputs.file(outputFile)
//
//     doLast {
//         val sourceSets = sourceSets["main"].runtimeClasspath.asPath
//         val checkingEvent = "[EVENT] Checking classpath capability conflict avoidance..."
//
//         val capabilityConflictsThatShouldBeAvoided = "javax.activation:activation:1.1"
//         val expectedCapability = "jakarta.activation/jakarta.activation-api/1.2.2"
//
//         val isError =
//             sourceSets.contains(capabilityConflictsThatShouldBeAvoided) || !sourceSets.contains(expectedCapability)
//
//         if (isError) {
//             throw GradleException(
//                 """      |
//           | ${checkingEvent}
//           |
//           |     Classpath should contain:
//           |      - '${expectedCapability}'
//           |     Classpath should NOT contain:
//           |      - '${capabilityConflictsThatShouldBeAvoided}.1'
//           |      - '${capabilityConflictsThatShouldBeAvoided}'
//           |     Classpath was: ${sourceSets}
//             """
//             )
//         } else {
//             val outputText = """      |
//       | ${checkingEvent}
//       |
//       |     Classpath correctly applied capability conflict avoidance
//       |
//       |        Contained:
//       |           - '${expectedCapability}'
//       |
//       |        Did not contain:
//       |           - '${capabilityConflictsThatShouldBeAvoided}.1'
//       |           - '${capabilityConflictsThatShouldBeAvoided}'
//       |           """
//
//             outputFile.get().asFile.writeText(outputText)
//             println(outputText)
//         }
//     }
// }

// tasks.test {
//     dependsOn(printRuntimeClasspathTask)
// }
