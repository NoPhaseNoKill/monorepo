plugins {
    id("com.nophasenokill.kotlin-library-plugin")
    id("com.google.devtools.ksp") version(libs.versions.kspSymbolProcessing.get())
}

dependencies {
    ksp(projects.standaloneProjects.libraries.kspProcessor)
}