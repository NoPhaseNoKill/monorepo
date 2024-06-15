plugins {
    id("com.nophasenokill.standalone-kotlin-base-plugin") version("1.0.0-local-dev") apply(false) // Enables the kotlin plugin at the root level so there aren't multiple instantiations at once
}

repositories {
    gradlePluginPortal()
}