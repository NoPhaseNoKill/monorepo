package com.nophasenokill.testcleanup.extension

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property


/**
 * Works with {@see TestFilesCleanupService} and {@see TestFilesCleanupServiceRootExtension}.
 * It collects states to be used in the build service for each project.
 */
interface TestFilesCleanupProjectState : TestFileCleanUpExtension {
    val projectPath: Property<String>
    val projectBuildDir: DirectoryProperty
}
