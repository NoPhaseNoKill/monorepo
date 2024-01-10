dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("../libs.versions.toml"))
    }
  }
}

rootDir.listFiles { file -> file.isDirectory && file.name.endsWith("-version-constraints") }?.forEach {
  include(it.name)
}


buildCache {
  local {
    isEnabled = true
    isPush = true
    removeUnusedEntriesAfterDays = 1

    directory = File(rootDir, "../../build-cache")
  }
}
