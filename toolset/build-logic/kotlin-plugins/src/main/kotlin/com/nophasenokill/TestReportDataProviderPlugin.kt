package com.nophasenokill

import com.nophasenokill.extensions.configureTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.DocsType
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.getValue

class TestReportDataProviderPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {

            // Disable the test report for the individual test task
            configureTask<Test>("test") {
                reports.html.required.set(false)
            }

            // Share the test report data to be aggregated for the whole project
            val binaryTestResultsElements: Configuration by configurations.creating {
                isCanBeConsumed = false
                isCanBeConsumed = true
                attributes {
                    attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
                    attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("test-report-data"))
                }

                val abstractTestTasks =  tasks.withType(AbstractTestTask::class.java)

                abstractTestTasks.forEach { testTask ->
                    this@creating.outgoing.artifact(testTask.binaryResultsDirectory.get())
                }
            }
        }
    }
}
