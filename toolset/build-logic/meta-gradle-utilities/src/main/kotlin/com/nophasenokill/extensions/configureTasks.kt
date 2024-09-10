package com.nophasenokill.extensions

import org.gradle.api.Project
import org.gradle.api.Task

inline fun <reified T : Task> Project.configureTasks(
    codeBlock: org.gradle.api.Action<T>
) {
    tasks.withType(T::class.java).configureEach(codeBlock)
}
