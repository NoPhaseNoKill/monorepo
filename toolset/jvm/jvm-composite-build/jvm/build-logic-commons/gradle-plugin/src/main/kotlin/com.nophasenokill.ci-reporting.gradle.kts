
import com.nophasenokill.testcleanup.TestFilesCleanupRootPlugin
import com.nophasenokill.testcleanup.extension.TestFileCleanUpExtension
import com.nophasenokill.testcleanup.extension.TestFilesCleanupBuildServiceRootExtension
import com.nophasenokill.testcleanup.extension.TestFilesCleanupProjectState

/**
 * When run from a Continuous Integration environment, we only want to archive a subset of reports, mostly for
 * failing tasks only, to not use up unnecessary disk space on Team City. This also improves the performance of
 * artifact publishing by reducing the artifacts and packaging reports that consist of multiple files.
 *
 * Reducing the number of reports also makes it easier to find the important ones when analysing a failed build in
 * Team City.
 */

val testFilesCleanup = extensions.create<TestFileCleanUpExtension>("testFilesCleanup").apply {
    reportOnly.convention(false)
}

if ("CI" in System.getenv() && project.name != "gradle-kotlin-dsl-accessors") {
    rootProject.plugins.apply(TestFilesCleanupRootPlugin::class.java)
    val globalExtension = rootProject.extensions.getByType<TestFilesCleanupBuildServiceRootExtension>()

    val projectState = objects.newInstance(TestFilesCleanupProjectState::class.java)

    globalExtension.projectStates.put(path, projectState)
    projectState.projectBuildDir = layout.buildDirectory
    projectState.projectPath = path
    projectState.reportOnly = testFilesCleanup.reportOnly
}
