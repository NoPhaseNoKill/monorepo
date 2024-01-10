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


buildCache {
  local {
    isEnabled = true
    isPush = true
    removeUnusedEntriesAfterDays = 1

    directory = File(rootDir, "../../build-cache")
  }
}
