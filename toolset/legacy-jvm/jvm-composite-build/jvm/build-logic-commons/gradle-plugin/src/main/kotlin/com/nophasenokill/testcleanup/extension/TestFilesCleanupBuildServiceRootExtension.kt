
package com.nophasenokill.testcleanup.extension

import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import java.io.File


/**
 * An extension to work with {@see TestFilesCleanupService}.
 * We have to collect all information we need in this extension and pass them
 * to the build service.
 */
interface TestFilesCleanupBuildServiceRootExtension {
    val projectStates: MapProperty<String, TestFilesCleanupProjectState>

    /**
     * Key is the path of a task, value is the possible report dirs it generates.
     */
    val taskPathToReports: MapProperty<String, List<File>>
}
