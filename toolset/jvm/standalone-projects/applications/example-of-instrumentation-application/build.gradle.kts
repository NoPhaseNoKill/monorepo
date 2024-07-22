plugins {
    id("com.nophasenokill.kotlin-application-plugin")
}

tasks.register<Copy>("instrumentApp") {
    dependsOn("compileTestKotlin")
    from("src/main/kotlin")
    into("$buildDir/instrumented")
    doLast {
        val instrumentedDir = file("$buildDir/instrumented")
        instrumentedDir.walk().filter { it.isFile && it.extension == "kt" }.forEach { file ->
            file.writeText("""
                 package com.nophasenokill;
                
                fun main() {
                    val app = ExampleOfInstrumentationApplicationApp()
                    app.run()
                }
                
                class ExampleOfInstrumentationApplicationApp {
                    fun run() {
                        println("Running instrumentation app")
                    }
                }
             """.trimIndent())
        }
    }
}

sourceSets {
    val test by getting {
        java.srcDirs("$buildDir/instrumented")
    }
}

tasks.named<Test>("test") {
    dependsOn("instrumentApp")
    doFirst {
        // Remove the main source set output
        classpath = classpath.filter { it.path != sourceSets["main"].output.classesDirs.asPath }

        // Add the instrumented files
        classpath = files("$buildDir/instrumented") + classpath
    }

    testLogging.showStandardStreams = true
}
