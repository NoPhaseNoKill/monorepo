plugins {
    // id("my-kotlin-plugin")
    alias(libs.plugins.kotlinJvm)
    `java-library`
    // id("library-plugin")
    // id("junit-test-plugin")
    // id("kotlin-lib-plugin")
}

tasks.compileJava {
    enabled = false
}

tasks.compileTestJava {
    enabled = false
}

tasks.processResources {
    enabled = false
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    // implementation(enforcedPlatform("com.nophasenokill.platform:platform"))

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

    // applies standard kotlin libs to projects
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    testImplementation(enforcedPlatform("org.junit:junit-bom"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    /*
        Both of these should error when uncommented due to to com.nophasenokill.dependency-analysis-project.gradle.kts.
        That plugin is meant to prevent project level dependency level declarations.

        To test it's still working, uncomment and run ./gradlew checkDependencyFormatting

        This comment has been copied into:
            - applications/app
            - libraries/list
            - libraries/utilities
    */
    // implementation("org.apache.commons:commons-text:1.10.0")
    // testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
}
