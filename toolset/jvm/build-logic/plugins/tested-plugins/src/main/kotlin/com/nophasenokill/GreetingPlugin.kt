package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project


class GreetingPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        
        with(project) {
            tasks.register("taskInsideGreetingPlugin") {
                plugins.apply(CommonsTestedPlugin::class.java)

                this.doLast {
                    logger.lifecycle("Hello from plugin 'greeting-plugin'")
                }
            }
        }
    }
}
