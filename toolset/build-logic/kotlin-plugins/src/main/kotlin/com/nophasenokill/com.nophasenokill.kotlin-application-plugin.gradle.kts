import gradle.kotlin.dsl.accessors._6ebf0b5d05ec1eff67605a516c5db18b.application
import gradle.kotlin.dsl.accessors._6ebf0b5d05ec1eff67605a516c5db18b.kotlin
import java.util.*

plugins {
    id("com.nophasenokill.kotlin-base-plugin")
    id("application")
}

afterEvaluate {
    /*
    Dynamically sets the application main class name to be the directory name.

    Example: If your directory is named application-one, this script will set
    the main class to ApplicationOneAppKt
 */

    val mainClassName = projectDir.name.split("-").joinToString("") { it ->
        it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    } + "AppKt"

    kotlin {
        application {
            mainClass.set("com.nophasenokill.${mainClassName}")
        }
    }
}

