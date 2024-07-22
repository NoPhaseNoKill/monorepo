package com.nophasenokill.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import java.io.File
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import java.security.MessageDigest

@CacheableTask
abstract class DirHashTask: DefaultTask() {

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val contents: DirectoryProperty

    @get:Input
    abstract val hashMethod: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun perform() {
        var md5 = ""
        val md = MessageDigest.getInstance(hashMethod.get())

        val path: String = contents.asFile.get().path
        File(path).walkTopDown()
            .filter { item -> Files.isRegularFile(item.toPath()) }
            .forEach {
                val digest = md.digest(it.readBytes())
                md5 += BigInteger(1, digest).toString(16)
            }

        outputDir.asFile.get().mkdirs()
        val hashFile = Paths.get(outputDir.asFile.get().absolutePath, "hash.txt")
        val digest = md.digest(md5.toByteArray())
        File(hashFile.toUri()).writeText(BigInteger(1, digest).toString(16))
    }
}