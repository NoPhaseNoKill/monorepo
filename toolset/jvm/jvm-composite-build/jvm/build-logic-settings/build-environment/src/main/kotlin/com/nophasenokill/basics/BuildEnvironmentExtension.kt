
package com.nophasenokill.basics

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property


interface BuildEnvironmentExtension {
    val gitCommitId: Property<String>
    val gitBranch: Property<String>
    val repoRoot: DirectoryProperty
    val rootProjectBuildDir: DirectoryProperty
}
