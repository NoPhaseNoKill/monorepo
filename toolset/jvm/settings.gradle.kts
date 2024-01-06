pluginManagement {
  repositories {
    gradlePluginPortal()
    // google()
  }
  includeBuild("gradle/plugins") // use this to include our own convention plugins
  includeBuild("gradle/settings") // use this to include our own convention plugins for settings.gradle.kts
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
    // google()
    maven("https://jitpack.io")
  }

  includeBuild("gradle/platform") // use this to include our own gradle platform to centralize version management
  // gradle/libs.versions.toml is automatically imported if exists
}

plugins {
  // my setting plugin that simply has some other setting plugins where versions are managed in version catalogs
  id("my.root-settings-plugins")
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
}

rootProject.name = "integraboost"

val libraries = mapOf(
  "sample-java-lib" to "libraries",
  "sample-kotlin-spring-lib" to "libraries",
)

val applications = mapOf(
  "sample-kotlin-app" to "applications",
  "sample-kotlin-spring-app" to "applications",
)

val projects = libraries + applications

// Allows custom declaration of paths while keeping the naming for task purposes nice and clean
projects.forEach { (name, type) ->
  include(":$name")
  project(":$name").projectDir = file("modules/$type/$name")
}
