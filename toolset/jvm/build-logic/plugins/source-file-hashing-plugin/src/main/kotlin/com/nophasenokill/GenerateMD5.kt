package com.nophasenokill

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.workers.WorkAction
import java.io.FileInputStream
import java.io.InputStream


// Do not implement the getParameters() method - Gradle will inject this at runtime
abstract class GenerateMD5 : WorkAction<MD5WorkParameters> {
    override fun execute() {
        try {
            val sourceFile = parameters.sourceFile.asFile.get()
            val md5File = parameters.mD5File.asFile.get()
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

