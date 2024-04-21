plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "basic-shared-test-extension"

includeBuild("gradle-problems-sample/sample-project")
includeBuild("gradle-problems-sample/meta-plugins/meta-plugin-one")
includeBuild("gradle-problems-sample/platforms/generalised-platform")
