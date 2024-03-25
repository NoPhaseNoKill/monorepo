plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "standalone-plugins"
include("standalone-plugin-one")
include("standalone-plugin-two")
