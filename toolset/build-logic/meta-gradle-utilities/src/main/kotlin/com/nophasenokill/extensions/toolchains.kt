package com.nophasenokill.extensions

val org.gradle.api.Project.`javaToolchains`: org.gradle.jvm.toolchain.JavaToolchainService get() =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("javaToolchains") as org.gradle.jvm.toolchain.JavaToolchainService
