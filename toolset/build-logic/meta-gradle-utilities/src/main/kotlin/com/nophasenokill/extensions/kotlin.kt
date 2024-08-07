package com.nophasenokill.extensions

import org.gradle.api.Action

fun org.gradle.api.Project.kotlin(configure: Action<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("kotlin", configure)


