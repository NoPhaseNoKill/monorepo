package com.nophasenokill.extensions

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

fun VersionCatalog.findCatalogPlugin(pluginToFind: String): String {
    return "findPlugin(pluginToFind).map { it.get().pluginId }.get()"
    // return findPlugin(pluginToFind)
    //     .orElseThrow { IllegalArgumentException("Expected to find plugin $pluginToFind but did not") }
    //     .map { it.pluginId }
}

fun Project.findCatalog(): VersionCatalog {
    return extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
}


// fun Project.findCatalog(): VersionCatalog {
//
//     return project.findPlugin(plugin).map {
//         it?.pluginId ?: throw IllegalArgumentException("Expected to find ${plugin} plugin but did not")
//     }
//
//     return requireNotNull(project.extensions.findByType(VersionCatalogsExtension::class.java)?.named("libs")) {
//         "Expected to find libs version catalog but did not"
//     }
// }

// fun VersionCatalog.findCatalogPlugin(plugin: String): String {
//     return requireNotNull(findPlugin(plugin).get().get().pluginId) {
//         "Expected to find ${plugin} plugin but did not"
//     }
// }

fun VersionCatalog.findCatalogLibrary(library: String): MinimalExternalModuleDependency {
    return findLibrary(library).get().get()
}


