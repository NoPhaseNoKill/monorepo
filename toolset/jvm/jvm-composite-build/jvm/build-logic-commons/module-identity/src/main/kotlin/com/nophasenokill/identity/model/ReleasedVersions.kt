package com.nophasenokill.identity.model


data class ReleasedVersions(
    val latestReleaseSnapshot: ReleasedVersion,
    val latestRc: ReleasedVersion,
    val finalReleases: List<ReleasedVersion>
)
