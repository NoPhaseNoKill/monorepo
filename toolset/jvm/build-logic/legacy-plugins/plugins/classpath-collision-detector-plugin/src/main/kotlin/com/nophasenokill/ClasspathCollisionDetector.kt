package com.nophasenokill

import com.nophasenokill.domain.ClasspathCollision
import com.nophasenokill.domain.ClasspathCollisionArtifact

class ClasspathCollisionDetector(
    private val collidedArtifacts: List<ClasspathCollisionArtifact>
) {

    private val entryToArtifacts: MutableMap<String, MutableList<ClasspathCollisionArtifact>> = HashMap()

    fun detectCollisions(): List<ClasspathCollision> {
        collidedArtifacts.forEach { scanArtifactContents(it) }
        return findCollidingEntries()
    }

    private fun findCollidingEntries(): List<ClasspathCollision> {
        return entryToArtifacts
            .filterValues { it.size > 1 }
            .map { ClasspathCollision(it.key, it.value) }
    }

    private fun scanArtifactContents(artifact: ClasspathCollisionArtifact) {
        artifact.contents.visit {
            if (!this.isDirectory) {
                val elementPath = this.path
                val artifactsWithFile = entryToArtifacts.getOrPut(elementPath) { mutableListOf() }
                artifactsWithFile.add(artifact)
                entryToArtifacts[elementPath] = artifactsWithFile
            }
        }
    }

}