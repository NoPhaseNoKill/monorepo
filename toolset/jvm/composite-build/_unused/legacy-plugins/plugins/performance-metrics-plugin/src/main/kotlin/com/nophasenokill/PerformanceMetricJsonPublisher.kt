package com.nophasenokill

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.publisher.Publisher
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.Serializable
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class PerformanceMetricJsonPublisher(private val buildDirectoryPath: String) : Publisher, Serializable {
    override fun publish(report: ExecutionReport) {
        try {
            val gson: Gson = GsonBuilder().setPrettyPrinting().create()

            val start = requireNotNull(report.beginMs) {
                "Build did not start and will not count to performance metrics"
            }

            val end = requireNotNull(report.endMs) {
                "Build did not finish and will not count to performance metrics"
            }

            val fileName = createFileName(start, end)

            val file = File(buildDirectoryPath, "historical-reports/performance-metrics/${fileName}.json")

            FileUtils.writeStringToFile(file, gson.toJson(report), null as String?, false)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun convertEpochToUTC(epoch: String): String {
        val instant: Instant = Instant.ofEpochMilli(epoch.toLong())
        val utc = instant.atZone(ZoneOffset.UTC)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        return utc.format(formatter)
    }

    private fun createFileName(startEpoch: String, endEpoch: String): String {

        val start = convertEpochToUTC(startEpoch)
        val end = convertEpochToUTC(endEpoch)
        return "${start}-to-${end}"
    }
}