package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project


class GreetingPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        with(project) {
            plugins.apply(CommonsTestedPlugin::class.java)

            task("taskInsideGreetingPlugin") {
                this.doLast {
                    logger.lifecycle("Hello from plugin 'greeting-plugin'")
                }
            }
        }
    }
}
