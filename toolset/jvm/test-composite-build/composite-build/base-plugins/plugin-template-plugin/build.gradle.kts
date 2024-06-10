plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

version = "1.0.0-local-dev"
group = "com.nophasenokill"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

val customConfiguration = configurations.register("customConfiguration") {
    // ...
}

dependencies {
    customConfiguration("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.23")
}

configurations {
    customConfiguration {
        attributes {
            attribute(
                Category.CATEGORY_ATTRIBUTE,
                project.objects.named(Category::class, Category.LIBRARY)
            )

            attribute(
                Usage.USAGE_ATTRIBUTE,
                project.objects.named(Usage::class, Usage.JAVA_RUNTIME)
            )

            // If you want to depend on a specific KGP variant:
            attribute(
                GradlePluginApiVersion.GRADLE_PLUGIN_API_VERSION_ATTRIBUTE,
                project.objects.named("gradle85")
            )
        }
    }
}
// org.jetbrains.intellij.deps:trove4j:1.0.20200330
// org.jetbrains.kotlin:kotlin-android-extensions:1.9.23
// org.jetbrains.kotlin:kotlin-assignment-compiler-plugin-embeddable:1.9.22
// org.jetbrains.kotlin:kotlin-build-common:1.9.22
// org.jetbrains.kotlin:kotlin-build-tools-api:1.9.22
// org.jetbrains.kotlin:kotlin-build-tools-impl:1.9.22
// org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.22
// org.jetbrains.kotlin:kotlin-compiler-runner:1.9.22
// org.jetbrains.kotlin:kotlin-daemon-client:1.9.22
// org.jetbrains.kotlin:kotlin-daemon-embeddable:1.9.22
// org.jetbrains.kotlin:kotlin-gradle-plugin-annotations:1.9.23
// org.jetbrains.kotlin:kotlin-gradle-plugin-api:1.9.23
// org.jetbrains.kotlin:kotlin-gradle-plugin-idea-proto:1.9.23
// org.jetbrains.kotlin:kotlin-gradle-plugin-idea:1.9.23
// org.jetbrains.kotlin:kotlin-gradle-plugin-model:1.9.23
// org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.23
// org.jetbrains.kotlin:kotlin-gradle-plugins-bom:1.9.23
// org.jetbrains.kotlin:kotlin-klib-commonizer-api:1.9.23
// org.jetbrains.kotlin:kotlin-klib-commonizer-embeddable:1.9.22
// org.jetbrains.kotlin:kotlin-native-utils:1.9.23
// org.jetbrains.kotlin:kotlin-project-model:1.9.23
// org.jetbrains.kotlin:kotlin-reflect:1.6.10
// org.jetbrains.kotlin:kotlin-sam-with-receiver-compiler-plugin-embeddable:1.9.22
// org.jetbrains.kotlin:kotlin-script-runtime:1.9.22
// org.jetbrains.kotlin:kotlin-scripting-common:1.9.22
// org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable:1.9.22
// org.jetbrains.kotlin:kotlin-scripting-compiler-impl-embeddable:1.9.22
// org.jetbrains.kotlin:kotlin-scripting-jvm:1.9.22
// org.jetbrains.kotlin:kotlin-stdlib:1.9.22
// org.jetbrains.kotlin:kotlin-tooling-core:1.9.23
// org.jetbrains.kotlin:kotlin-util-io:1.9.23
// org.jetbrains.kotlin:kotlin-util-klib:1.9.23
// org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.0
// org.jetbrains:annotations:13.0

// gradle.beforeProject {
//
// }



dependencies {
    // implementation("org.jetbrains:annotations:13.0")
    // implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.23"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.23")

    configurations.all {
        isTransitive = false
    }

    // kotlinCompilerClasspath("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.22")
    // kotlinCompilerClasspath("org.jetbrains.kotlin:kotlin-stdlib:1.9.23")
    // kotlinCompilerClasspath("org.jetbrains:annotations:24.1.0")
    // kotlinCompilerClasspath("org.jetbrains.kotlin:kotlin-script-runtime:1.9.23")
    // kotlinCompilerClasspath("org.jetbrains.kotlin:kotlin-daemon-embeddable:1.9.23")
    // kotlinCompilerClasspath("org.jetbrains.intellij.deps:trove4j:1.0.20200330")
    //
    // kotlinCompilerPluginClasspathMain("org.jetbrains.kotlin:kotlin-scripting-compiler-impl-embeddable:1.9.23")
    // kotlinCompilerPluginClasspathMain("org.jetbrains.kotlin:kotlin-scripting-common:1.9.23")
    // kotlinCompilerPluginClasspathMain("org.jetbrains.kotlin:kotlin-scripting-jvm:1.9.23")
    //
    // kotlinCompilerPluginClasspathTest("org.jetbrains.kotlin:kotlin-scripting-compiler-impl-embeddable:1.9.23")
    // kotlinCompilerPluginClasspathTest("org.jetbrains.kotlin:kotlin-scripting-common:1.9.23")
    // kotlinCompilerPluginClasspathTest("org.jetbrains.kotlin:kotlin-scripting-jvm:1.9.23")
    //
    // testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    // testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2") // may be required in future when using junit params
    // testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    // testImplementation("org.opentest4j:opentest4j:1.3.0")
    // testImplementation("org.junit.platform:junit-platform-commons:1.10.2")
    // testImplementation("org.junit.platform:junit-platform-engine:1.10.2")
    //
    // testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}