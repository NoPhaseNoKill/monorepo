package com.nophasenokill.extensions

import org.gradle.api.Project

inline fun <reified PluginExtension> Project.configurePlugin(
    noinline codeBlock: PluginExtension.() -> Unit
) {
    val pluginExtension = extensions.getByType(PluginExtension::class.java)

    pluginExtension.apply {
        apply {
            codeBlock()
        }
    }
}
