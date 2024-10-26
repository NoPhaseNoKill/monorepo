package com.nophasenokill.identity.extension

import com.google.gson.Gson
import com.nophasenokill.identity.model.ReleasedVersions
import org.gradle.api.file.RegularFile
import org.gradle.util.GradleVersion
import org.gradle.util.internal.VersionNumber


class ReleasedVersionsDetails(currentBaseVersion: GradleVersion, releasedVersionsFile: RegularFile) {
    val allPreviousVersions: List<GradleVersion>
    val mostRecentRelease: GradleVersion
    val mostRecentSnapshot: GradleVersion

    val allTestedVersions: List<GradleVersion>

    val mainTestedVersions: List<GradleVersion>
    val lowestInterestingVersion: GradleVersion
    val lowestTestedVersion: GradleVersion

    init {
        lowestTestedVersion = GradleVersion.version("3.0")
        lowestInterestingVersion = GradleVersion.version("0.8")

        val releasedVersions = releasedVersionsFile.asFile.reader().use {
            Gson().fromJson(it, ReleasedVersions::class.java)
        }

        val latestFinalRelease = releasedVersions.finalReleases.first()
        val latestRelease =
            listOf(releasedVersions.latestReleaseSnapshot, releasedVersions.latestRc)
                .filter {
                    it.gradleVersion() > latestFinalRelease.gradleVersion()
                }.maxByOrNull { it.buildTimeStamp() }
                ?: latestFinalRelease
        val previousVersions = (listOf(latestRelease) + releasedVersions.finalReleases)
            .filter {
                it.gradleVersion() >= lowestInterestingVersion &&
                    it.gradleVersion().baseVersion < currentBaseVersion
            }.distinct()
        allPreviousVersions = previousVersions.map { it.gradleVersion() }
        mostRecentRelease = previousVersions.first().gradleVersion()
        mostRecentSnapshot = releasedVersions.latestReleaseSnapshot.gradleVersion()

        val testedVersions = previousVersions.filter { it.gradleVersion() >= lowestTestedVersion }
        // Only use latest patch release of each Gradle version
        allTestedVersions = testedVersions.map { VersionNumber.parse(it.gradleVersion().version) }
            .groupBy { "${it.major}.${it.minor}" }
            .map { (_, v) -> v.maxOrNull()!!.format() }

        // Limit to first and last release of each major version
        mainTestedVersions = testedVersions.map { VersionNumber.parse(it.gradleVersion().version) }
            .groupBy { it.major }
            .map { (_, v) -> listOf(v.minOrNull()!!.format(), v.maxOrNull()!!.format()) }.flatten()
    }

    private
    fun VersionNumber.format() =
        // reformat according to our versioning scheme, since toString() would typically convert 1.0 to 1.0.0
        GradleVersion.version("$major.${minor}${if (micro > 0) ".$micro" else ""}${if (qualifier != null) "-$qualifier" else ""}")
}
