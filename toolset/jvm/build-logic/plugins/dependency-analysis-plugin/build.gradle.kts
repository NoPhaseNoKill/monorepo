
plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("com.autonomousapps:dependency-analysis-gradle-plugin:${libs.versions.dependencyAnalysisGradlePlugin.get()}")
}