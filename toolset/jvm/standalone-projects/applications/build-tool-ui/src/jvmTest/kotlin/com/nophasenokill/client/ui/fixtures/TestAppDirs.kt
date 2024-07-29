package com.nophasenokill.client.ui.fixtures

import com.nophasenokill.client.core.files.AppDirs
import java.io.File

class TestAppDirs(
    private val rootDir: File
) : AppDirs {

    override val configurationDirectory = sub("config")
    override val dataDirectory = sub("data")
    override val logDirectory = sub("log")
    override val cacheDirectory = sub("cache")
    override val temporaryDirectory = sub("tmp")

    private fun sub(name: String): File =
        rootDir.resolve(name).also { it.mkdirs() }
}
