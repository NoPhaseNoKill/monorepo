/**
 * Settings plugin used by the root settings.gradle.kts that simply just apply some other setting plugins,
 * where versions are managed by the grade/platform through version catalogs.
 */
plugins {
  id("com.gradle.enterprise")
  id("org.gradle.toolchains.foojay-resolver-convention")
}

buildCache {
  local {
    isEnabled = true
    isPush = true
    removeUnusedEntriesAfterDays = 1

    directory = File(rootDir, "build-cache")
  }
}
