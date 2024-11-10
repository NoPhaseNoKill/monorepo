// init.gradle.kts

// import java.io.File
//
// println("Running init script...")
//
// val initializedFile = File("${System.getProperty("user.dir")}/build/initialized.txt")
// val localM2Dir = File(System.getProperty("user.home"), ".m2/repository")
// val localRepoDir = File("${System.getProperty("user.dir")}/local-repo")
//
// // Function to check if the initialization is complete
// fun isInitialized(): Boolean {
//     return initializedFile.exists() && initializedFile.readText().trim() == "true"
// }
//
// // Function to perform the actual plugin publishing in a separate Gradle process
// fun publishPlugins() {
//     println("Publishing plugins to local-repo...")
//
//     // Create `initialized.txt` if it doesn’t exist
//     if (!initializedFile.exists()) {
//         initializedFile.parentFile.mkdirs() // Ensure the directory exists
//         initializedFile.writeText("false") // Initialize the file with "false" indicating not yet initialized
//         println("Created initialized.txt with initial content 'false'.")
//     }
//
//     // Run the Gradle command to publish to mavenLocal for the plugin subproject
//     val result = exec {
//         commandLine = listOf("./gradlew", ":plugin:publishToMavenLocal")
//     }
//
//     if (result.exitValue == 0) {
//         // Simulate a delay (optional, for testing purposes)
//         Thread.sleep(2000)
//
//         // Copy files from mavenLocal to the local repo
//         localRepoDir.mkdirs()
//         copyMavenLocalToLocalRepo(localM2Dir, localRepoDir)
//
//         // Update `initialized.txt` to signal completion
//         initializedFile.writeText("true")
//         println("Publishing complete. `initialized.txt` updated to 'true'.")
//     } else {
//         throw GradleException("Failed to publish plugins.")
//     }
// }
//
// // Function to copy files from mavenLocal to the local repo
// fun copyMavenLocalToLocalRepo(sourceDir: File, destinationDir: File) {
//     println("Copying plugins from mavenLocal to local-repo...")
//     sourceDir.walkTopDown().filter { it.isFile && it.path.contains("com/nophasenokill") }.forEach { file ->
//         val destFile = File(destinationDir, file.relativeTo(sourceDir).path)
//         destFile.parentFile.mkdirs()
//         file.copyTo(destFile, overwrite = true)
//     }
// }
//
// // Main execution logic in the init script
// if (!isInitialized()) {
//     println("Initialization file not found or incomplete. Running publish process...")
//     publishPlugins()
// } else {
//     println("Initialization already completed.")
// }
//
// // Polling loop to wait until the file is updated
// var attempts = 0
// while (!isInitialized()) {
//     println("Waiting for initialization to complete...")
//     Thread.sleep(1000) // Check every second
//     attempts++
//     if (attempts >= 10) { // Timeout after 10 seconds
//         println("Initialization timeout reached. Exiting build.")
//         throw GradleException("Initialization did not complete within the expected time.")
//     }
// }
//
// println("Initialization complete. Proceeding with settings evaluation.")

// gradle.beforeSettings {
//     gradle.rootProject {
//
//
//         tasks.register("alpha")  {
//             doLast {
//                 println("Hello from alpha")
//             }
//
//             setProperty("isInitialized", false)
//         }
//
//         tasks.register("beta")  {
//             doLast {
//                 println("Hello from beta")
//             }
//
//             setProperty("isInitialized", true)
//         }
//
//
//         project.hasProperty("isInitialized")
//
//         if (project.findProperty("isInitialized") == true) {
//             tasks.named("alpha").get().finalizedBy(tasks.named("beta"))
//         }
//     }
//
// }
