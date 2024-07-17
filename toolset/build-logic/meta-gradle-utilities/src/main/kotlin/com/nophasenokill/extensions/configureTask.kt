package com.nophasenokill.extensions

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider

inline fun <reified T : Task> Project.configureTask(
    taskName: String,
    noinline codeBlock: T.() -> Unit
) {
    return tasks.named(taskName, T::class.java).configure(codeBlock)
}

inline fun <reified T : Task> configureTask(
    taskProvider: TaskProvider<T>,
    noinline codeBlock: T.() -> Unit
) {
    return taskProvider.configure(codeBlock)
}