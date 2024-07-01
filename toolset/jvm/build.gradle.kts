

tasks.register("buildAll") {
    rootDir.walk().forEach { file ->
        if (file.isFile && file.name == "build.gradle.kts") {
            // Includes any projects
            val parentFileRelativeToRootDir = file.parentFile.relativeTo(rootDir)
            val replacedRelativePath = parentFileRelativeToRootDir.path.replace(File.separator, ":")

            /*
                Excludes the root dir
             */
            if(parentFileRelativeToRootDir.path.isNotEmpty() && replacedRelativePath !== "") {
                dependsOn(":$replacedRelativePath:build")
            }
        }
    }
}



