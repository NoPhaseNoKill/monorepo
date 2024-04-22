package com.nophasenokill

import org.gradle.internal.impldep.org.junit.platform.engine.discovery.DiscoverySelectors
import org.gradle.internal.impldep.org.junit.platform.engine.reporting.ReportEntry
import org.gradle.internal.impldep.org.junit.platform.launcher.Launcher
import org.gradle.internal.impldep.org.junit.platform.launcher.TestExecutionListener
import org.gradle.internal.impldep.org.junit.platform.launcher.TestIdentifier
import org.gradle.internal.impldep.org.junit.platform.launcher.TestPlan
import org.gradle.internal.impldep.org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.gradle.internal.impldep.org.junit.platform.launcher.core.LauncherFactory
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class TestListenerExample {

    class CustomListener : TestExecutionListener {
        override fun reportingEntryPublished(testIdentifier: TestIdentifier, entry: ReportEntry) {
            val entryMap = entry.keyValuePairs
            if (testIdentifier.isTest) {
                Logging.getLogger("SharedAppExtension").lifecycle("Captured: " + testIdentifier.displayName +
                        ", stdout: " + entryMap["stdout"]!!.trim { it <= ' ' } +
                        ", stderr: " + entryMap["stderr"]!!.trim { it <= ' ' })
            }
        }
    }

    @Test
    fun test1() {
        Logging.getLogger("SharedAppExtension").lifecycle("System.out.println: test1()")
        System.err.Logging.getLogger("SharedAppExtension").lifecycle("System.err.println: test1()")
    }

    @Test
    fun test2() {
        Logging.getLogger("SharedAppExtension").lifecycle("System.out.println: test2()")
        System.err.Logging.getLogger("SharedAppExtension").lifecycle("System.err.println: test2()")

        val logger = LoggerFactory.getLogger(this::class.java)
        logger.info("LoggerFactory.getLogger(this::class.java) logger.info")
    }

    fun main() {
        // val something = LogRecordListener()
        val request = LauncherDiscoveryRequestBuilder.request()
            .selectors(DiscoverySelectors.selectPackage("test"))
            .configurationParameter("junit.platform.output.capture.stdout", "true")
            .configurationParameter("junit.platform.output.capture.stderr", "true")
            .build()
        LauncherFactory.openSession().use { session ->
            val launcher: Launcher = session.launcher

            // val listener = LoggingListener.forJavaUtilLogging(Level.FINEST)

            launcher.registerTestExecutionListeners(CustomListener())

            val testPlan: TestPlan = launcher.discover(request)
            launcher.execute(testPlan)
        }


    }

}