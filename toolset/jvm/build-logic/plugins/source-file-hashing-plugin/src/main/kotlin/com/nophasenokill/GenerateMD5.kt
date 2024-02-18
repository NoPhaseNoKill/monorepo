package com.nophasenokill

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.workers.WorkAction
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


// Do not implement the getParameters() method - Gradle will inject this at runtime
abstract class GenerateMD5 : WorkAction<MD5WorkParameters> {

    @InputFile
    val sourceFile: File = parameters.sourceFile.asFile.get()

    @OutputFile
    val md5File: File = parameters.mD5File.asFile.get()

    override fun execute() {
        try {
            val stream: InputStream = FileInputStream(sourceFile)
            println("Generating MD5 for " + sourceFile.name + "...")
            // Artificially make this task slower.
            Thread.sleep(3000)
            FileUtils.writeStringToFile(md5File, DigestUtils.md5Hex(stream), null as String?)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

