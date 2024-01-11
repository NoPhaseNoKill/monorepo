// Dynamically includes top level directories within each of the modules' sub-folders
val directories = setOf("applications", "libraries")
directories.forEach { dir ->
    rootDir
        .resolve("modules/${dir}")
        .listFiles { file -> file.isDirectory && !file.isHidden }
        ?.forEach {
            include("modules:$dir:${it.name}")
    }
}