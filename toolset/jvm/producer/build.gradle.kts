plugins {
    kotlin("jvm") version "2.0.21"
}

val outputDir = layout.buildDirectory.dir("output")

tasks.register("generateOutput") {
    outputs.dir(outputDir)
    doLast {
        val outputFile = outputDir.get().file("data.txt").asFile
        outputFile.parentFile.mkdirs()
        outputFile.writeText("This is the producer's output data.")
        println("Generated output at ${outputFile.absolutePath}")
    }
}

artifacts {
    add("default", outputDir.get().file("data.txt")) {
        builtBy(tasks.named("generateOutput"))
    }
}
