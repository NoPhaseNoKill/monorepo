package com.nophasenokill.domain

data class ClasspathCollision(
    val entry: String,
    val artifacts: MutableList<ClasspathCollisionArtifact>
)