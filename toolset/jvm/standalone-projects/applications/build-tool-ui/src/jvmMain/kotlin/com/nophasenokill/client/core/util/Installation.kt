package com.nophasenokill.client.core.util

import com.nophasenokill.client.core.Constants.APPLICATION_NAME
import com.nophasenokill.client.core.files.AppDirs
import com.nophasenokill.client.core.files.RealAppDirs
import java.io.File
import java.nio.charset.Charset
import java.security.MessageDigest

val appDirs: AppDirs by lazy {
    RealAppDirs(
        appName = APPLICATION_NAME,
        installationIdentifier = calculateInstallationIdentifier()
    )
}

private val ownExecutable = File(ProcessHandle.current().info().command().get())

private class JarProbe

@OptIn(ExperimentalStdlibApi::class)
private fun calculateInstallationIdentifier(): String {
    val ownJar = File(JarProbe::class.java.protectionDomain.codeSource.location.toURI())
    val installIdentifierDir =
        if (ownExecutable.nameWithoutExtension == "java") ownJar.parentFile
        else ownExecutable.parentFile
    val hashBytes = MessageDigest.getInstance("MD5").digest(
        installIdentifierDir.absolutePath.toByteArray(Charset.defaultCharset())
    )
    // TODO consider using a shorter hash encoding
    return hashBytes.toHexString()
}
