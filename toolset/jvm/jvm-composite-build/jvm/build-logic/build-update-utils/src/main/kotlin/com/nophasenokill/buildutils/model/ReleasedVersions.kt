
package com.nophasenokill.buildutils.model


data class ReleasedVersions(
    val latestReleaseSnapshot: ReleasedVersion,
    val latestRc: ReleasedVersion,
    val finalReleases: List<ReleasedVersion>
)
