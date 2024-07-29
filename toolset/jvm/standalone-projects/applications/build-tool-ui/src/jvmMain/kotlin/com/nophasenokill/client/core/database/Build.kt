package com.nophasenokill.client.core.database

import com.nophasenokill.client.core.gradle.GradleDistribution
import com.nophasenokill.client.core.util.generateIdentity
import java.io.File

data class Build(
    val id: String = generateIdentity(),
    val rootDir: File,
    val javaHomeDir: File? = null,
    val gradleUserHomeDir: File? = null,
    val gradleDistribution: GradleDistribution = GradleDistribution.Default,
)
