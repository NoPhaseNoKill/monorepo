plugins {
    alias(libs.plugins.kotlinJvm) apply(false)
}

/*
    Dynamically includes any projects (something with a build.gradle.kts)
    and excludes the root project.
 */
tasks.register("buildAll") {
    rootDir.walk().forEach { file ->
        if (file.isFile && file.name == "build.gradle.kts") {
            val parentFileRelativeToRootDir = file.parentFile.relativeTo(rootDir)
            val replacedRelativePath = parentFileRelativeToRootDir.path.replace(File.separator, ":")

            if(parentFileRelativeToRootDir.path.isNotEmpty() && replacedRelativePath !== "") {
                dependsOn(":$replacedRelativePath:build")
            }
        }
    }
}