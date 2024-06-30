rootProject.name = "extended-plugin"

pluginManagement {
    /*
        This implicitly also brings in:
            plugins {
               id("org.jetbrains.kotlin.jvm") version("1.9.22")
            }
     */
    includeBuild("../root-settings-plugin")
    includeBuild("../build-service-warning-fix-plugin")
    includeBuild("../hash-source-plugin")
}

plugins {
    id("com.nophasenokill.root-settings-plugin")
}

dependencyResolutionManagement {
    versionCatalogs {
        create(defaultLibrariesExtensionName.get()) {
            from(files("../../../gradle/libs.versions.toml"))
        }
    }
}

/*
    Used to allow getting shared configuration
 */
include("basic-plugin")
project(":basic-plugin").projectDir = file("../basic-plugin")