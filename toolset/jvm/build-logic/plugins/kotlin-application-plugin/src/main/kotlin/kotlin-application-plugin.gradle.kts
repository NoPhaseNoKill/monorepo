plugins {
    id("commons-plugin")
    id("org.jetbrains.kotlin.jvm")
    id("application")
    id("dependency-analysis-project")
}

application {
    mainClass.set("com.nophasenokill.app.AppKt")
}