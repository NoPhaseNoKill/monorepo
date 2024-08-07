package com.nophasenokill.extensions

import org.gradle.api.Project


val Project.sourceSets: org.gradle.api.tasks.SourceSetContainer get() =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("sourceSets") as org.gradle.api.tasks.SourceSetContainer