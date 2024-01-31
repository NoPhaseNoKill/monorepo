package com.nophasenokill

import org.gradle.api.Plugin
import org.gradle.api.Project


class GreetingPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        project.task("taskInsideGreetingPlugin") {
            this.doLast {
                println("Hello from plugin 'com.nophasenokill.greetingPlugin'")
            }
        }

        // with(project) {
        //     project.tasks.register("taskInsideGreetingPlugin") { task: Task ->
        //         task.doLast {
        //             println("Hello from plugin 'com.nophasenokill.greetingPlugin'")
        //         }
        //     }
        // }

        // createProjectPlugin(project) {
        //
        //
        // }
    }
}

// fun <R> createProjectPlugin(project: Project, block: Project.() -> R) {
//     // wraps it nicely and lowers amount of text we need to continually type when creating new ones due to scoping
//     return with(project) {
//         block()
//     }
// }




// class CommonsPluginTested : Plugin<Project> {
//     override fun apply(project: Project) {
//         with(project) {
//
//             plugins.apply("java")
//             plugins.apply("org.jetbrains.kotlin.jvm")
//
//             // ... (Other plugin configurations)
//
//             fun<T> getLazilyEvaluatedValue(value: T): Provider<T> = provider { value }
//
//             val javaVersion = getLazilyEvaluatedValue(JavaVersion.VERSION_21)
//             val currentJavaVersion = getLazilyEvaluatedValue(JavaVersion.current())
//             val javaLanguageVersion = getLazilyEvaluatedValue(JavaLanguageVersion.of(javaVersion.get().toString()))
//             val buildJavaHome = getLazilyEvaluatedValue(System.getProperty("java.home"))
//
//             gradle.beforeProject {
//                 if (currentJavaVersion.get() != javaVersion.get()) {
//                     throw GradleException("This build requires JDK ${javaVersion.get()}. It's currently ${buildJavaHome.get()}.")
//                 }
//             }
//
//             // ... (Other configurations like tasks and dependencies)
//
//             tasks.register("compileAll") {
//                 group = LifecycleBasePlugin.BUILD_GROUP
//                 description = "Compile all Java code"
//                 it.dependsOn(tasks.withType(JavaCompile::class.java))
//             }
//
//             tasks.register("testAll") {
//                 group = LifecycleBasePlugin.VERIFICATION_GROUP
//                 description = "Test all Java code"
//                 it.dependsOn(tasks.withType(Test::class.java))
//             }
//         }
//     }
// }
