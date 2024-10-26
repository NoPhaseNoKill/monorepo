

// Ensure the archives produced are reproducible
tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
    dirPermissions { unix("0755") }
    filePermissions { unix("0644") }
}
