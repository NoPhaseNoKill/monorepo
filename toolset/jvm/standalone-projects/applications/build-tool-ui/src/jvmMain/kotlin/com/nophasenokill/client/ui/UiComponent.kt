package com.nophasenokill.client.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import com.nophasenokill.client.core.database.BuildsRepository
import com.nophasenokill.client.core.files.AppDirs
import com.nophasenokill.client.core.gradle.GradleConnectionParameters
import com.nophasenokill.client.ui.build.BuildComponent
import com.nophasenokill.client.ui.buildlist.BuildListComponent
import com.nophasenokill.client.ui.connected.ConnectedComponent

class UiComponent(
    context: ComponentContext,
    private val appDispatchers: AppDispatchers,
    private val appDirs: AppDirs,
    private val buildsRepository: BuildsRepository,
) : ComponentContext by context {

    sealed interface Child {
        class BuildList(val component: BuildListComponent) : Child
        class Build(val component: BuildComponent) : Child
        class Connected(val component: ConnectedComponent) : Child
    }

    private val navigation = StackNavigation<Config>()

    val childStack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.BuildList,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    private fun createChild(config: Config, context: ComponentContext): Child =
        when (config) {
            Config.BuildList -> Child.BuildList(
                BuildListComponent(
                    context = context,
                    appDispatchers = appDispatchers,
                    buildsRepository = buildsRepository,
                    onBuildSelected = { id -> navigation.push(Config.Build(id)) }
                )
            )

            is Config.Build -> Child.Build(
                BuildComponent(
                    context = context,
                    appDispatchers = appDispatchers,
                    appDirs = appDirs,
                    buildsRepository = buildsRepository,
                    id = config.id,
                    onConnect = { inputs -> navigation.push(Config.Connected(inputs)) },
                    onFinished = { navigation.pop() }
                )
            )

            is Config.Connected -> Child.Connected(
                ConnectedComponent(
                    context = context,
                    appDispatchers = appDispatchers,
                    parameters = config.inputs,
                    onFinished = { navigation.pop() },
                )
            )
        }

    @Serializable
    private sealed class Config {
        @Serializable
        data object BuildList : Config()

        @Serializable
        data class Build(val id: String) : Config()

        @Serializable
        data class Connected(val inputs: GradleConnectionParameters) : Config()
    }
}
