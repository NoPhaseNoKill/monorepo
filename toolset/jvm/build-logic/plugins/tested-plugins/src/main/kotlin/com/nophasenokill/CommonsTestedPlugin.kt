package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project


class CommonsTestedPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        // Register task lazily instead of eagerly
        project.tasks.register("taskInsideCommonsTestedPlugin") {
            this.doLast {
                logger.lifecycle("Hello from plugin 'commons-tested-plugin'")
            }
        }
    }
}
