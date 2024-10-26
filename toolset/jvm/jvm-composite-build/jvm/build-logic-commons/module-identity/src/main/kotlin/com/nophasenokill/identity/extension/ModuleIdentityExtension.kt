package com.nophasenokill.identity.extension

import com.nophasenokill.basics.buildCommitId
import com.nophasenokill.identity.tasks.BuildReceipt
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.*
import org.gradle.util.GradleVersion


abstract class ModuleIdentityExtension(val tasks: TaskContainer, val objects: ObjectFactory) {

    abstract val version: Property<GradleVersion>

    abstract val baseName: Property<String>

    abstract val buildTimestamp: Property<String>
    abstract val snapshot: Property<Boolean>
    abstract val promotionBuild: Property<Boolean>

    abstract val releasedVersions: Property<ReleasedVersionsDetails>

    fun createBuildReceipt() {
        val createBuildReceipt by tasks.registering(BuildReceipt::class) {
            this.version = this@ModuleIdentityExtension.version.map { it.version }
            this.baseVersion = this@ModuleIdentityExtension.version.map { it.baseVersion.version }
            this.snapshot = this@ModuleIdentityExtension.snapshot
            this.promotionBuild = this@ModuleIdentityExtension.promotionBuild
            this.buildTimestampFrom(this@ModuleIdentityExtension.buildTimestamp)
            this.commitId = project.buildCommitId
            this.receiptFolder = project.layout.buildDirectory.dir("generated-resources/build-receipt")
        }
        tasks.named<Jar>("jar").configure {
            from(createBuildReceipt.map { it.receiptFolder })
        }
    }
}
