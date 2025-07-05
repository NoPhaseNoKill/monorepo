package com.nophasenokill.extensions

import org.gradle.api.Action

fun org.gradle.api.Project.kotlinJvm(configure: Action<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("kotlin", configure)
