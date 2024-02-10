

plugins {
    `kotlin-dsl`
}

dependencies {
    // requires version for current implementation, ideally we should have central repo for declaration of versions (maybe toml)

    implementation("com.autonomousapps:dependency-analysis-gradle-plugin:1.29.0") {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-common")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-scripting-common")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-scripting-jvm")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-compiler-embeddable")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-daemon-client")
        exclude(group = "org.checkerframework", module = "checker-qual")
        exclude(group = "com.google.errorprone", module = "error_prone_annotations")
    }
}
