package com.nophasenokill

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.WorkQueue
import org.gradle.workers.WorkerExecutor
import javax.inject.Inject

/*
    Old non-parallel version is this:

    abstract class CreateMD5 : SourceTask() {
        @get:OutputDirectory
        abstract val destinationDirectory: DirectoryProperty

        @TaskAction
        fun createHashes() {
            for (sourceFile in source.files) {
                try {
                    val stream: InputStream = FileInputStream(sourceFile)
                    println("Generating MD5 for " + sourceFile.name + "...")
                    // Artificially make this task slower.
                    Thread.sleep(3000)
                    val md5File = destinationDirectory.file(sourceFile.name + ".md5")
                    FileUtils.writeStringToFile(md5File.get().asFile, DigestUtils.md5Hex(stream), null as String?)
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        }
    }

 */

abstract class CreateMD5 : SourceTask() {
    @get:OutputDirectory
    abstract val destinationDirectory: DirectoryProperty

    @get:Inject
    abstract val workerExecutor: WorkerExecutor

    @TaskAction
    fun createHashes() {
        val workQueue: WorkQueue = workerExecutor.noIsolation()

        for (sourceFile in source.files) {
            val md5File: Provider<RegularFile> = destinationDirectory.file(sourceFile.name + ".md5")

            workQueue.submit(GenerateMD5::class.java) {
                this.sourceFile.set(sourceFile)
                this.mD5File.set(md5File)
            }
        }
    }
}


