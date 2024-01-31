plugins {
    id("commons-plugin")
    id("application")
    id("dependency-analysis-project")
}

application {
    mainClass.set("com.nophasenokill.app.AppKt")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}