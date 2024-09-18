package com.nophasenokill

import com.nophasenokill.service.TaskEventsService
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.build.event.BuildEventsListenerRegistry
import javax.inject.Inject


abstract class TaskEventsPlugin : Plugin<Project> {
    @get:Inject
    abstract val eventsListenerRegistry: BuildEventsListenerRegistry // Use service injection to obtain an instance of the BuildEventsListenerRegistry.

    override fun apply(project: Project) {

        val serviceProvider: Provider<TaskEventsService> =
         /*
             Note: Rather than passing in an action as suggested by: https://docs.gradle.org/8.5/userguide/build_services.html#operation_listener

                 val action: (BuildServiceSpec<BuildServiceParameters.None>).() -> Unit = {}

                 project.gradle.sharedServices.registerIfAbsent(
                     "taskEvents",
                     TaskEventsService::class.java,
                     action
                 )

             We do not need to declare this in kotlin, and can instead omit it, due to the underlying code calling

                 return registerIfAbsent(name, implementationType, ignore -> {});
         */
            project.gradle.sharedServices.registerIfAbsent(
                "taskEvents",
                TaskEventsService::class.java,
            )

        serviceProvider.get().registerPluginListener(project)

        // Use the service Provider to subscribe the build service to build events.
        eventsListenerRegistry.onTaskCompletion(serviceProvider)
    }
}
