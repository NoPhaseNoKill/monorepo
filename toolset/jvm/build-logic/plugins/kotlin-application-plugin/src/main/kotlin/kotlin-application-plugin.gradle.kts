plugins {
    id("commons-plugin")
    id("application")
    id("com.nophasenokill.dependency-analysis-project")
}

application {
    mainClass.set("com.nophasenokill.app.AppKt")
}