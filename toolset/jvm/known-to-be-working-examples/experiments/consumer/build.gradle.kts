val sharedConfiguration: Configuration by configurations.creating {
    isCanBeConsumed = false
}

dependencies {
    sharedConfiguration(project(path = ":known-to-be-working-examples:plugins:producer", configuration = "sharedConfiguration"))
}

tasks.register("showFile") {
    val sharedFiles: FileCollection = sharedConfiguration
    inputs.files(sharedFiles)
    doFirst {
        logger.lifecycle("Shared file contains the text: '{}'", sharedFiles.singleFile.readText())
    }
}
