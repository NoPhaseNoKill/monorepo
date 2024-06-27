package com.nophasenokill

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import java.io.File
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import java.security.MessageDigest

/*
    Create a cacheable MD5 hash of the source contents
    of the project where this task is run.

    This was shown in:
     https://dpeuniversity.gradle.com/learning_paths/b82f8dd7-d61e-450f-820e-3e719ef70bee/courses/54469478-55ba-416d-9cef-3b5aa33dd2a5/activities/ed919eb1-354d-4024-acfb-d8954c70af1b
 */

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