plugins {
    id("commons-plugin")
    id("application")
    id("dependency-analysis-project")
}

application {
    mainClass.set("com.nophasenokill.app.AppKt")
}

dependencies {
    /*
        This is not required, but is being implicitly included
     */
    // implementation("org.jetbrains.kotlin:kotlin-stdlib")
}