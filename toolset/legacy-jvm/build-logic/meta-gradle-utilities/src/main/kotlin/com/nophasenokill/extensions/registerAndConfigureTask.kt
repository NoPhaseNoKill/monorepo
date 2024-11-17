package com.nophasenokill.extensions

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider

inline fun <reified T : Task> Project.registerAndConfigureTask(
    taskName: String,
    noinline codeBlock: T.() -> Unit
): TaskProvider<T> {
    return tasks.register(taskName, T::class.java, codeBlock)
}