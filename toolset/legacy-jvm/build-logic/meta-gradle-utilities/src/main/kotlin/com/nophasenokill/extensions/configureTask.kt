package com.nophasenokill.extensions

import org.gradle.api.Project
import org.gradle.api.Task

inline fun <reified T : Task> Project.configureTask(
    taskName: String,
    noinline codeBlock: T.() -> Unit
) {
    tasks.named(taskName, T::class.java).configure(codeBlock)
}
