package com.nophasenokill.domain

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/*
    Gradle measures everything in milliseconds since the epoch.
    This class allows us to easily get both the formatted string
    for logs, and the millsSinceEpoch is primarily which used for the
    listeners that require this value.

    It also has the added benefit of lazily creating these values,
    and deferring any execution until absolutely necessary.

    For example:

        abstract class TaskEventsService : BuildService<BuildServiceParameters.None>,
            OperationCompletionListener,
            OperationResult,
            ProgressListener,
            ProgressEvent
        {
            override fun getStartTime(): Long {
                val time = GradleTime.now()
                println("Start time is: ${time.current}")
                return time.current
            }
        }
 */

class GradleTime private constructor(private val millisSinceEpoch: Lazy<Long>) {

    val current: Long
        get() {
            println("Instantiating current")
            return millisSinceEpoch.value
        }

    val currentFormatted: String
        get() {
            println("Instantiating currentFormatted")
            return getFormatted().value
        }

    private fun getFormatted(): Lazy<String> {
        val instant = lazy {
            println("Instantiating instant")
            Instant.ofEpochMilli(millisSinceEpoch.value).also {
                println("millisSinceEpoch.value inside instant")
            }
        }
        val localDateTime = lazy {
            println("Instantiating localDateTime")
            LocalDateTime.ofInstant(instant.value, ZoneId.systemDefault())
        }
        val formatter = lazy {
            println("Instantiating formatter")
            /*
                Due to DateTimeFormatter.ofPattern() using Locale getDefault(), it means that it outputs lowercase
                am/pm . Upon further investigation, it is recommended to always specify a locale when using
                DateTimeFormatter.

                Note: If this is ever switched to a predefined formatter, from here: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/format/DateTimeFormatter.html#predefined
                these all work with the system locale, and hence this should not be required.

                For more details, see https://stackoverflow.com/questions/40025528/java-text-parseexception-unparseable-date-java-text-dateformat-parsedateforma/65544056#65544056
             */
            DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm:ss.SSSa").withLocale(Locale.ENGLISH)
        }
        return lazy {
            println("Instantiating getCurrentFormatted")
            localDateTime.value.format(formatter.value)
        }
    }

    companion object {
        fun now(): GradleTime {
            return GradleTime(
                lazy {
                    println("Instantiating current time millis")
                    System.currentTimeMillis()
                }
            )
        }
    }

    override fun toString(): String {
        println("Instantiating toString")
        return getFormatted().value
    }
}
