plugins {
    id("com.nophasenokill.kotlin-library-plugin")
    id("com.google.devtools.ksp") version(libs.versions.kspSymbolProcessing.get())
}

dependencies {
    implementation("com.squareup:javapoet:${libs.versions.squareUpJavaPoet.get()}")
    implementation("com.google.devtools.ksp:symbol-processing-api:${libs.versions.kspSymbolProcessing.get()}")
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}