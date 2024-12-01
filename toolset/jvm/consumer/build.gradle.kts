plugins {
    kotlin("jvm") version "2.0.21"
}

dependencies {
    // Use the producer's output
    implementation(files(provider {
        val producerBuild = project.gradle.includedBuild("producer")
        producerBuild.projectDir.resolve("build/output/data.txt")
    }))
}



tasks.register("consumeOutput") {

    val outputDir = layout.buildDirectory.dir("output")

    outputs.dir(outputDir)
    inputs.file(provider {
        val producerBuild = project.gradle.includedBuild("producer")
        producerBuild.projectDir.resolve("build/output/data.txt")
    })

    doLast {

        val producerOutput = inputs.files.singleFile

        val outputFile = outputDir.get().file("data.txt").asFile
        outputFile.parentFile.mkdirs()
        outputFile.writeText("Consumer received: ${producerOutput.readText()}")
    }
}

tasks.named("build") {
    dependsOn("consumeOutput")
}

