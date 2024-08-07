package com.nophasenokill.extensions

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.ModuleIdentifier
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider


fun Project.findCatalog(): VersionCatalog {
    return requireNotNull(project.extensions.findByType(VersionCatalogsExtension::class.java)?.named("libs")) {
        "Expected to find libs version catalog but did not"
    }
}

fun VersionCatalog.findCatalogPlugin(plugin: String): String {
    return requireNotNull(findPlugin(plugin).get().get().pluginId) {
        "Expected to find ${plugin} plugin but did not"
    }
}

fun VersionCatalog.findCatalogLibrary(plugin: String): Provider<MinimalExternalModuleDependency> {
    return requireNotNull(findLibrary(plugin).get()) {
        "Expected to find ${plugin} plugin but did not"
    }
}


