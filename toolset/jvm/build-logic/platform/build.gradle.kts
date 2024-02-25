

plugins {
    id("java-platform")
    id("dependency-analysis-platform")
}

group = "com.nophasenokill.platform"

// Do not use this. This includes the dependencies
// javaPlatform.allowDependencies()


dependencies {
    constraints {
        api("com.autonomousapps:dependency-analysis-gradle-plugin:${libs.versions.dependencyAnalysisGradlePlugin.get()}")
        api("com.google.code.gson:gson:${libs.versions.gson.get()}")
        api("com.google.errorprone:error_prone_annotations:${libs.versions.errorProneAnnotations.get()}")
        api("io.github.cdsap:talaiot:${libs.versions.talaiot.get()}")
        api("io.github.cdsap.talaiot:talaiot:${libs.versions.talaiot.get()}")
        api("io.github.cdsap.talaiot:io.github.cdsap.talaiot.gradle.plugin:${libs.versions.talaiot.get()}")
        api("jakarta.activation:jakarta.activation-api:${libs.versions.jakartaActivationApi.get()}")
        api("javax.activation:activation:${libs.versions.javaxActivation.get()}")
        api("org.apache.commons:commons-text:${libs.versions.commonsText.get()}")
        api("commons-io:commons-io:${libs.versions.commonsIo.get()}")
        api("commons-codec:commons-codec:${libs.versions.commonsCodec.get()}")
        api("org.checkerframework:checker-qual:${libs.versions.checkerQual.get()}")
        api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${libs.versions.kotlin.get()}")
        api("org.jetbrains.kotlin:kotlin-bom:${libs.versions.kotlin.get()}") {
            because("It matches the version of ${libs.versions.springBootDependencies.get()} for spring")
        }

        api("org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}")

        api("org.junit:junit-bom:${libs.versions.junit.get()}")

        api("org.springframework.boot:spring-boot-dependencies:${libs.versions.springBootDependencies.get()}") {
            because("It matches the version of ${libs.versions.dependencyAnalysisGradlePlugin.get()} for kotlin bom")
        }
    }
}