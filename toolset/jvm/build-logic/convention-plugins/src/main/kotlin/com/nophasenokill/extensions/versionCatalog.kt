package com.nophasenokill.extensions

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

fun Project.findCatalog(): VersionCatalog {
    return extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
}

fun VersionCatalog.findCatalogVersion(versionIdentifier: String): String {
    return findVersion(versionIdentifier).get().toString()
}

fun VersionCatalog.findCatalogLibrary(library: String): MinimalExternalModuleDependency {
    return findLibrary(library).get().get()
}
