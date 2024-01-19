plugins {
    id("commons-plugin")
    id("application")
    id("dependency-analysis-project")
}

application {
    mainClass.set("com.nophasenokill.app.AppKt")
}