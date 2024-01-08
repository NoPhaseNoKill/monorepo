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