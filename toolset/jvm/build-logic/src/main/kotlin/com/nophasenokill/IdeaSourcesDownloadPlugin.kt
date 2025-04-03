package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project

class IdeaSourcesDownloadPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            plugins.apply("com.nophasenokill.idea-sources-download-plugin")
        }
    }
}
