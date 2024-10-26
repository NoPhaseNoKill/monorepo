plugins {
    id("com.nophasenokill.build-logic.kotlin-dsl-gradle-plugin")
    id("com.nophasenokill.build-logic.groovy-dsl-gradle-plugin")
}

description = "Provides a plugin that cleans up after executing tests"

errorprone {
    disabledChecks.addAll(
        "CatchAndPrintStackTrace", // 1 occurrences
        "DefaultCharset", // 3 occurrences
        "JavaTimeDefaultTimeZone", // 1 occurrences
    )
}

dependencies {
    implementation("com.nophasenokill:basics")
    implementation("com.nophasenokill:module-identity")
}
