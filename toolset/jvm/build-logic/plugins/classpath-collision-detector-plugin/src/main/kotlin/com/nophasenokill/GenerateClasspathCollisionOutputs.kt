package com.nophasenokill

import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.apache.commons.io.FileUtils
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.workers.WorkAction
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

@CacheableTask
abstract class GenerateClasspathCollisionOutputs : WorkAction<ClasspathCollisionParameters> {

    @InputFile
    val artifactFile: File = parameters.artifactInputFile.asFile.get()

    @OutputFile
    val artifactOutputFile: File = parameters.artifactOutputFile.asFile.get()


    override fun execute() {
        try {
            val stream: InputStream = FileInputStream(artifactFile)
            println("Generating classpath collision output for " + artifactFile.name + "...")
            // Artificially make this task slower.
            Thread.sleep(3000)
            FileUtils.writeStringToFile(artifactOutputFile, DigestUtils.md5Hex(stream), null as String?)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}