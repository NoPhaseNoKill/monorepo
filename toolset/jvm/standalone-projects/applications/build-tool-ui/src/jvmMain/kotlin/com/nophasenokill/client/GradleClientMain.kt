package com.nophasenokill.client

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.configureSwingGlobalsForCompose
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.nophasenokill.client.core.Constants.APPLICATION_DISPLAY_NAME
import com.nophasenokill.client.core.database.BuildsRepository
import com.nophasenokill.client.core.database.sqldelight.ApplicationDatabaseFactory
import com.nophasenokill.client.core.database.sqldelight.SqlDriverFactory
import com.nophasenokill.client.core.files.AppDirs
import com.nophasenokill.client.core.util.appDirs
import com.nophasenokill.client.ui.AppDispatchers
import com.nophasenokill.client.ui.UiComponent
import com.nophasenokill.client.ui.UiContent
import org.slf4j.LoggerFactory
import javax.swing.SwingUtilities

private val logger = LoggerFactory.getLogger(com.nophasenokill.client.GradleClientMain::class.java)

fun main() {

    appDirs.logApplicationDirectories()

    val sqlDriverFactory = SqlDriverFactory(appDirs)
    val sqlDriver = sqlDriverFactory.createDriver()
    Runtime.getRuntime().addShutdownHook(
        Thread {
            sqlDriverFactory.stopDriver(sqlDriver)
            logger.atInfo().log { "DB stopped" }
        }
    )
    val database = ApplicationDatabaseFactory().createDatabase(sqlDriver).also {
        logger.atInfo().log { "DB started" }
    }

    GradleClientMain(
        appDirs,
        BuildsRepository(database.buildsQueries)
    ).run()
}

class GradleClientMain(
    private val appDirs: AppDirs,
    private val buildsRepository: BuildsRepository
) : Runnable {

    @OptIn(ExperimentalComposeUiApi::class)
    override fun run() {

        // Should be called before using any class from java.swing.*
        // (even before SwingUtilities.invokeLater or MainUIDispatcher)
        configureSwingGlobalsForCompose()

        val lifecycle = LifecycleRegistry()
        val uiComponent = com.nophasenokill.client.runOnUiThread {
            UiComponent(
                context = DefaultComponentContext(lifecycle = lifecycle),
                appDispatchers = AppDispatchers(),
                appDirs = appDirs,
                buildsRepository = buildsRepository,
            )
        }

        application {
            val windowState = rememberWindowState(size = DpSize(1800.dp, 1200.dp))

            Window(
                state = windowState,
                onCloseRequest = ::exitApplication, title = APPLICATION_DISPLAY_NAME) {
                LaunchedEffect(Unit) {
                    logger.atInfo().log { "$APPLICATION_DISPLAY_NAME started!" }
                }
                UiContent(uiComponent)
            }
        }
    }
}

private fun <T> runOnUiThread(block: () -> T): T {
    if (SwingUtilities.isEventDispatchThread()) {
        return block()
    }

    var error: Throwable? = null
    var result: T? = null

    @Suppress("TooGenericExceptionCaught")
    SwingUtilities.invokeAndWait {
        try {
            result = block()
        } catch (e: Throwable) {
            error = e
        }
    }

    error?.also { throw it }

    @Suppress("UNCHECKED_CAST")
    return result as T
}
