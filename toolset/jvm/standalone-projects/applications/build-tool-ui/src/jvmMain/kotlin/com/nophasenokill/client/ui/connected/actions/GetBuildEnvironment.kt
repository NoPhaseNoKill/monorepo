package com.nophasenokill.client.ui.connected.actions

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.nophasenokill.client.ui.composables.horizontalScrollContent
import org.gradle.tooling.model.build.BuildEnvironment

class GetBuildEnvironment : GetModelAction<BuildEnvironment> {

    override val modelType = BuildEnvironment::class

    override val displayName: String
        get() = "Build Environment"

    @Composable
    override fun ColumnScope.ModelContent(model: BuildEnvironment) {
        horizontalScrollContent {
            Text(
                text = "Build Environment",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Build Identifier: ${model.buildIdentifier.rootDir}",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "Java Home: ${model.java.javaHome}",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "JVM Arguments:  ${model.java.jvmArguments}",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "Gradle Version: ${model.gradle.gradleVersion}",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "Gradle User Home: ${model.gradle.gradleUserHome}",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
