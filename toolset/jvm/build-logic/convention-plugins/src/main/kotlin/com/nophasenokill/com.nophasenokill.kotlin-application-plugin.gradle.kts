import java.util.*

plugins {
    id("com.nophasenokill.kotlin-base-plugin")
    id("application")
}

val mainClassName = projectDir.name.split("-").joinToString("") { it ->
    it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
} + "AppKt"

application {
    mainClass.set("com.nophasenokill.${mainClassName}")
}
