package com.nophasenokill.extensions

import org.gradle.api.Action
import org.gradle.api.Project


val Project.sourceSets: org.gradle.api.tasks.SourceSetContainer get() =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("sourceSets") as org.gradle.api.tasks.SourceSetContainer

fun org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension.sourceSets(configure: Action<org.gradle.api.NamedDomainObjectContainer<org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet>>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("sourceSets", configure)