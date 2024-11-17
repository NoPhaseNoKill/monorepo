package com.nophasenokill.identity.model

import org.gradle.util.GradleVersion

import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


data class ReleasedVersion(val version: String, val buildTime: String) {
    fun gradleVersion() = GradleVersion.version(version)
    fun buildTimeStamp(): Date = SimpleDateFormat("yyyyMMddHHmmssZ").apply { timeZone = TimeZone.getTimeZone("UTC") }.parse(buildTime)
}
