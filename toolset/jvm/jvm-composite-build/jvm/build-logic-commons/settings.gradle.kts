dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
}

// Make sure all the build-logic is compiled for the right Java version
gradle.lifecycle.beforeProject {
    pluginManager.withPlugin("java-base") {
        the<JavaPluginExtension>().toolchain {
            languageVersion = JavaLanguageVersion.of(17)
            vendor = JvmVendorSpec.ADOPTIUM
        }
    }
}

includeBuild("../build-logic-settings")

rootProject.name = "build-logic-commons"

include("basics")
include("build-platform")
include("gradle-plugin")
include("module-identity")

apply(from = "../gradle/shared-with-buildSrc/mirrors.settings.gradle.kts")
