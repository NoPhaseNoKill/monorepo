package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project


class CommonsTestedPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        project.task("taskInsideCommonsTestedPlugin") {
            this.doLast {
                logger.lifecycle("Hello from plugin 'commons-tested-plugin'")
            }
        }
    }
}