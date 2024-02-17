package com.nophasenokill

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.publisher.Publisher
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.Serializable


class MyJsonPublisher(private val buildDirectoryPath: String) : Publisher, Serializable {
    override fun publish(report: ExecutionReport) {
        try {
            val gson: Gson = GsonBuilder().setPrettyPrinting().create()
            val file = File(buildDirectoryPath, "reports/performance-metrics/data.json")

            FileUtils.writeStringToFile(file, gson.toJson(report), null as String?, false)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}