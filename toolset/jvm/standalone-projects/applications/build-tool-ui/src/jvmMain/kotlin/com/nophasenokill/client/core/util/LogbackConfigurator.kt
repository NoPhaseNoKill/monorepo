package com.nophasenokill.client.core.util

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.classic.spi.Configurator
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.spi.ContextAwareBase
import ch.qos.logback.core.util.FileSize
import com.nophasenokill.client.core.Constants.APPLICATION_NAME

// Configures logback debug logging to file
// https://logback.qos.ch/manual/configuration.html
class LogbackConfigurator : ContextAwareBase(), Configurator {

    @Suppress("MagicNumber")
    private companion object {
        const val MAX_HISTORY = 30
        const val FILE_SIZE_CAP = 1L * 1024 * 1024 * 1024 // 1GB
    }

    override fun configure(loggerContext: LoggerContext): Configurator.ExecutionStatus {

        addInfo("Setting up $APPLICATION_NAME logging configuration.")

        val logDir = appDirs.logDirectory
        val logFilename = "application"
        val logFile = logDir.resolve("$logFilename.log")

        val appender = RollingFileAppender<ILoggingEvent>().apply {
            context = loggerContext
            name = "file-logging"
            file = logFile.toString()

            val fileAppender = this
            rollingPolicy = TimeBasedRollingPolicy<ILoggingEvent>().apply {
                context = loggerContext
                fileNamePattern = "$logDir/%d{yyyy/MM}/$logFilename.gz"
                maxHistory = MAX_HISTORY
                setTotalSizeCap(FileSize(FILE_SIZE_CAP))
                setParent(fileAppender)
                start()
            }

            addFilter(
                ThresholdFilter().apply {
                    context = loggerContext
                    setLevel("DEBUG")
                    start()
                }
            )

            encoder = PatternLayoutEncoder().apply {
                context = loggerContext
                pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{128} -%kvp- %msg%n"
                start()
            }

            start()
        }

        val rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME)
        rootLogger.level = Level.DEBUG
        rootLogger.addAppender(appender)

        return Configurator.ExecutionStatus.INVOKE_NEXT_IF_ANY
    }
}
