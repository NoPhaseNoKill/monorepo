package com.nophasenokill.domain

import org.gradle.api.file.FileTree
import java.io.File

data class ClasspathCollisionArtifact(
    val file: File,
    val contents: FileTree
)