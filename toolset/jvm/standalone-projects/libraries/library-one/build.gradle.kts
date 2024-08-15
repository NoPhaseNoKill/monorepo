plugins {
    id("com.nophasenokill.kotlin-library-plugin")
    id("com.google.devtools.ksp") version(libs.versions.kspSymbolProcessing.get())
}

evaluationDependsOnChildren()

dependencies {
    ksp(projects.kspProcessor)
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

tasks.compileJava {
    dependsOn(tasks.named("kspKotlin"))

}

tasks.compileTestJava {
    dependsOn(tasks.named("kspTestKotlin"))
}