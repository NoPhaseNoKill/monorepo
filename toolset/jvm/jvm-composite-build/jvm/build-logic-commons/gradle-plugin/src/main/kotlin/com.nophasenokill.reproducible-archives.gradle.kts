
/*
    Alters platform specific things to make build reproducible.
     For instance, different default encoring on Windows versus Unix
 */

tasks.withType<JavaCompile>().configureEach {
    options.apply {
        isFork = true
        encoding = "UTF-8"
        compilerArgs.add("-implicit:none")
        compilerArgs.add("-Werror")
        compilerArgs.add("-Xlint:all,-serial")
    }
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true

    filePermissions {
        // read 0644, rw-r--r--
        unix("rw-r--r--")
    }
    dirPermissions {
        // read &execute 0755, rwxr-xr-x
        unix("rwxr-xr-x")
    }
}
