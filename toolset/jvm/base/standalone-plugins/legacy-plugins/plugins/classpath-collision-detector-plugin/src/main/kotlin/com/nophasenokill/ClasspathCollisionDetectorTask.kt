package com.nophasenokill

import com.nophasenokill.domain.ClasspathCollisionArtifact
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ArchiveOperations
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.*
import org.gradle.api.tasks.util.PatternSet
import org.gradle.workers.WorkQueue
import org.gradle.workers.WorkerExecutor
import java.io.File
import javax.inject.Inject

@CacheableTask
abstract class ClasspathCollisionDetectorTask : DefaultTask() {

    @get:InputFiles
    @get:Classpath
    abstract val configurations: ConfigurableFileCollection

    @get:Input
    abstract val collisionExclusions: ListProperty<String>

    @get:Inject
    protected abstract val archiveOperations: ArchiveOperations

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @get:Inject
    abstract val workerExecutor: WorkerExecutor

    @TaskAction
    fun checkClasspathCollisions() {
        val workQueue: WorkQueue = workerExecutor.noIsolation()
        val artifacts: List<ClasspathCollisionArtifact> = findClasspathArtifacts()

        val collisionDetector = ClasspathCollisionDetector(artifacts)
        val collisions = collisionDetector.detectCollisions()

        if(collisions.isNotEmpty()) {
            val reportableCollisions: MutableSet<String> = mutableSetOf()

            collisions.forEach {
                val collisionsJoined = it.artifacts.joinToString(",\n       ", prefix = "\n       ",  postfix = "\n      ") { it.file.toString() }
                reportableCollisions.add(collisionsJoined)
            }
            val formatted = reportableCollisions.map { it }.toString()
            throw GradleException("     Classpath collision(s) found. \n       Collisions are: \n      $formatted")
        }

        for (artifact in artifacts) {

            val artifactName = artifact.file.name
            val outputFile = outputDirectory.file("$artifactName.md5")

            workQueue.submit(GenerateClasspathCollisionOutputs::class.java) {
                this.artifactInputFile.set(artifact.file)
                this.artifactOutputFile.set(outputFile)
            }
        }
    }

    private fun findClasspathArtifacts(): List<ClasspathCollisionArtifact> {
        return configurations.files
            .filter { it.name.endsWith(".jar") }
            .map { toClassPathArtifact(it) }
    }

    private fun toClassPathArtifact(file: File): ClasspathCollisionArtifact {
        val artifactContents = archiveOperations.zipTree(file)
        val patternSet = PatternSet().exclude(collisionExclusions.get())
        val filteredContents = artifactContents.matching(patternSet)
        return ClasspathCollisionArtifact(file, filteredContents)
    }
}