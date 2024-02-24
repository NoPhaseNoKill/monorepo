package com.nophasenokill

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.workers.WorkParameters

interface ClasspathCollisionParameters : WorkParameters {
    val artifactInputFile: RegularFileProperty
    val artifactOutputFile: RegularFileProperty
}