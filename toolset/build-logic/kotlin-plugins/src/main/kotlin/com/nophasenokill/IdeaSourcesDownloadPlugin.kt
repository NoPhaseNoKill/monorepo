package com.nophasenokill

import com.nophasenokill.extensions.configurePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.plugins.ide.idea.IdeaPlugin

class IdeaSourcesDownloadPlugin: Plugin<Project> {
    override fun apply(project: Project) {

        project.run {

            gradle.beforeSettings {
                pluginManager.withPlugin("java") {
                    plugins.apply("idea")

                    configurePlugin<IdeaPlugin> {
                        model.module {
                            isDownloadSources = true
                        }
                    }
                }
            }
        }
    }
}
