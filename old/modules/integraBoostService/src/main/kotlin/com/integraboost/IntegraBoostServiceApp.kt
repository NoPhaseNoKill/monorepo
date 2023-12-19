package com.integraboost

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class IntegraBoostServiceApp

fun main(args: Array<String>) {
    runApplication<IntegraBoostServiceApp>(*args)
}