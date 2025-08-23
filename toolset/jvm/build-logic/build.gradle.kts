import org.gradle.api.tasks.testing.logging.TestLogEvent

/*
    IMPORTANT NOTE: These are the 'internal' plugins that are then re-exposed outside the plugin block.

    This has several advantages:
        1) It ensures that build logic can be used within the build-logic build.gradle.kts (this file) itself
        2) From the rootDir (aka toolset/jvm) we can just include a single build (build-logic) and use the plugins
        from within the rootDir build.gradle.kts file.

    If we didn't choose to do it like this, we would end up in a position where the rootDir build would need to include
    all builds separately (ie build-logic/convention-plugins, build-logic/some-other-included build) which becomes gross (see
    below). This maintains a single point of entry in rootDir.

    You'd expect that if the build-logic was being included from the rootDir, that because build-logic also has an
    included build of convention plugins - that you'd get this for free, and not need to do this. However, this is
    NOT the case - and if we don't re-export things here, we EXPLICITLY need to include the exact build to be able to
    import plugins from the rootDir/build.gradle.kts file - otherwise we get a can't be found plugin error.

    A basic example of why this is how it is as follows:

        rootDir includes includeBuild("build-logic")
            -> build-logic -> includes includeBuild("convention-plugins")
                -> convention-plugins includes all of our plugins

        Expectation: When using includeBuild("build-logic") from rootDir - that you'd get convention plugins for free

        Reality: Plugin not found error

    So instead, we basically just do a simple re-export of the plugins from any included builds inside build-logic,
    and avoid this issue.
 */

plugins {
    // Notice that these are NOT the exposed plugins, aka these are the internal ones being used
    id("com.nophasenokill.java-version-checker-plugin")
    id("com.nophasenokill.plugin-base")
    id("com.nophasenokill.group-and-version-details-plugin")
    id("com.nophasenokill.repositories-plugin")
    id("com.nophasenokill.kotlin-base-plugin")
}

gradlePlugin {
    plugins {
        create("javaVersionCheckerPlugin") {
            id = "com.nophasenokill.exposed.java-version-checker-plugin"
            implementationClass = "com.nophasenokill.JavaVersionCheckerPlugin"
        }

        create("pluginBase") {
            id = "com.nophasenokill.exposed.plugin-base"
            implementationClass = "com.nophasenokill.PluginBase"
        }

        create("groupAndVersionDetailsPlugin") {
            id = "com.nophasenokill.exposed.group-and-version-details-plugin"
            implementationClass = "com.nophasenokill.GroupAndVersionDetailsPlugin"
        }

        create("repositoriesPlugin") {
            id = "com.nophasenokill.exposed.repositories-plugin"
            implementationClass = "com.nophasenokill.RepositoriesPlugin"
        }

        create("kotlinBasePlugin") {
            id = "com.nophasenokill.exposed.kotlin-base-plugin"
            implementationClass = "com.nophasenokill.KotlinBasePlugin"
        }

        create("kotlinApplicationPlugin") {
            id = "com.nophasenokill.exposed.kotlin-application-plugin"
            implementationClass = "com.nophasenokill.KotlinApplicationPlugin"
        }

        create("kotlinDesktopApplicationPlugin") {
            id = "com.nophasenokill.exposed.kotlin-desktop-application-plugin"
            implementationClass = "com.nophasenokill.KotlinDesktopApplicationPlugin"
        }

        create("kotlinLibraryPlugin") {
            id = "com.nophasenokill.exposed.kotlin-library-plugin"
            implementationClass = "com.nophasenokill.KotlinLibraryPlugin"
        }

        create("ideaSourcesDownloadPlugin") {
            id = "com.nophasenokill.exposed.idea-sources-download-plugin"
            implementationClass = "com.nophasenokill.IdeaSourcesDownloadPlugin"
        }

        create("incrementalTestPlugin") {
            id = "com.nophasenokill.exposed.incremental-test-plugin"
            implementationClass = "com.nophasenokill.IncrementalTestPlugin"
        }
    }
}

dependencies {
    implementation("com.nophasenokill.convention-plugins:convention-plugins:${libs.versions.groupVersion.get()}")


    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    implementation("org.jetbrains.compose:compose-gradle-plugin:${libs.versions.composePlugin.get()}")

    // only exists from v2 onwards
    if(libs.versions.kotlin.get() >= "2.0.0") {
        implementation("org.jetbrains.kotlin:compose-compiler-gradle-plugin:${libs.versions.kotlin.get()}")
    } else {
        println("""
            Not including org.jetbrains.kotlin:compose-compiler-gradle-plugin dependency. Found kotlin version needs to be
            2.0.0 or above for this dependency to make sense
        """.trimIndent())
    }

    // used for incremental task caching
    implementation("org.ow2.asm:asm:9.7")
    implementation("org.ow2.asm:asm-commons:9.7")
    implementation("org.ow2.asm:asm-util:9.7")
    implementation("org.benf:cfr:0.152")

}

tasks.test {
    useJUnitPlatform()
    testLogging.events = setOf(
        TestLogEvent.STANDARD_OUT,
        TestLogEvent.STARTED,
        TestLogEvent.PASSED,
        TestLogEvent.SKIPPED,
        TestLogEvent.FAILED,
        TestLogEvent.STANDARD_OUT,
        TestLogEvent.STANDARD_ERROR,
    )

    testLogging.minGranularity = 2
}
