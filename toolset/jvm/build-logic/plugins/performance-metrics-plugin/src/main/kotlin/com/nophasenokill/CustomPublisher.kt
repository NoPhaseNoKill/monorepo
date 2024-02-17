package com.nophasenokill

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.publisher.Publisher

class CustomPublisher : Publisher {

    override fun publish(report: ExecutionReport) {
        println("[CustomPublisher] : Number of tasks = ${report.tasks?.size}")
        println("[CustomPublisher] : Kotlin = ${report.customProperties.buildProperties["kotlin"]}")
        println("[CustomPublisher] : Java = ${report.customProperties.buildProperties["java"]}")
        println("[CustomPublisher] : PID = ${report.customProperties.taskProperties["pid"]}")
    }
}


