dependencyResolutionManagement {
  repositories {
    gradlePluginPortal()
  }
  versionCatalogs {
    create("libs") {
      from(files("../libs.versions.toml"))
    }
  }
}

include("root-settings-plugins")


gradle.projectsEvaluated {
  val buildCachePath = extra.get("buildCachePath") as String

  buildCache {
    local {
      isEnabled = true
      isPush = true
      removeUnusedEntriesAfterDays = 1

      directory = File(buildCachePath)
    }
  }
}
