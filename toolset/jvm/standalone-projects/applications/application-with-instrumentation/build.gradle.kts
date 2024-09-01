import com.nophasenokill.GradleDirectory.getBuildDirectory
import com.nophasenokill.extensions.sourceSets
import java.util.*

plugins {
    
    id("com.nophasenokill.kotlin-application-plugin")
}



// tasks.register<Copy>("instrumentApp") {
//     val buildDir = project.getBuildDirectory().get()
//     dependsOn("compileKotlin")
//     from(sourceSets["main"].kotlin.sourceDirectories)
//     into("${buildDir.asFile.absolutePath}/instrumented")
//
//     doLast {
//         val instrumentedDir = file("${buildDir}/instrumented")
//
//         instrumentedDir.walk().filter { it.isFile && it.extension == "kt" && it.name.contains(getMainClassName()) }.forEach { file ->
//             file.writeText("""
//                 package com.nophasenokill;
//
//                 fun main() {
//                     val appOutput = TestInstrumentationApp.doSomething()
//                     println(appOutput)
//                 }
//
//                 object TestInstrumentationApp {
//                     fun doSomething(): String {
//                         val calculatedValue = Calculator.add()
//                         assert(calculatedValue == 4) {
//                             "Expected calculator/calculator value to be present and equal to 4 and was not."
//                         }
//                         return "App HAS been instrumented"
//                     }
//                 }
//             """.trimIndent())
//         }
//     }
// }


/*
    The dependency tree/ordering of these is very specific, and you need to be careful
    not to introduce regressions.

    We want:
        1. The kotlin files to be compiled
        2. Start the instrumentation based on those
        3. Modify the classpath of our tests to be all source included source sets, and replacing any non-instrumented classes with our instrumented versions
            i) We need don't necessarily need all source sets, but if we're trying to do things at runtime with ASM,
                this will mean we don't run into issues where it doesn't have access to things it needs
        4. Run the tests

    If we do not do this, in this specific order, we run into ordering issues from either:
        1. A completely fresh build
        2. When using cached values/determining whether tasks need to be re-run somewhere down the track
 */

// sourceSets {
//     val instrumented by creating {
//         kotlin.srcDir("${project.layout.buildDirectory.get()}/instrumented")
//         compileClasspath += sourceSets["main"].output + configurations["compileClasspath"]
//         runtimeClasspath += output + compileClasspath
//     }
//
//     val test by getting {
//         compileClasspath += sourceSets["instrumented"].output
//         runtimeClasspath += sourceSets["instrumented"].output
//     }
// }
//
// tasks.named("compileInstrumentedKotlin") {
//     // dependsOn("instrumentApp")
// }
//
// tasks.named<Test>("test") {
//     dependsOn("compileInstrumentedKotlin")
//     mustRunAfter("compileInstrumentedKotlin")
//
//     val instrumented = files(sourceSets["instrumented"].output.classesDirs)
//
//     doFirst {
//
//         // Ensure the test classpath includes the instrumented files and excludes the original files
//         // TODO THIS DOESNT LOOK RIGHT BUT ITS SOMEHOW WORKING?
//         classpath = classpath.filter {
//             println("File being included at ${it.absolutePath}")
//             !it.absolutePath.contains("build/classes/kotlin/main") && !it.absolutePath.contains("build/classes/java/main")
//         } + instrumented
//     }
//
//     testLogging.showStandardStreams = true
// }
