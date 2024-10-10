pluginManagement {
    repositories {
        gradlePluginPortal()
    }

    println("Ohai")

    fun findFileUpwards(startingDir: File, fileName: String): File? {
        var currentDir: File? = startingDir

        while (currentDir != null && currentDir.exists()) {
            val file = File(currentDir, fileName)
            if (file.exists()) {
                return file
            }
            // Move one level up
            currentDir = currentDir.parentFile
        }

        // Return null if the file is not found up to the root directory
        return null
    }

    /*
        This is called by root, as well as any other included builds and hence must go back to the $rootDir to find
        the file correctly.
     */
    println("Attempting to get kotlin-meta-dependencies file from: ${file("${rootProject.projectDir.path}/kotlin-meta-dependencies.txt")}")


    val startingDir = rootProject.projectDir
    val fileName = "kotlin-meta-dependencies.txt"

    val kotlinMetaDependenciesFile = findFileUpwards(startingDir, fileName)

    if (kotlinMetaDependenciesFile != null) {
        println("Found file at: ${kotlinMetaDependenciesFile.absolutePath}")
        val lines = kotlinMetaDependenciesFile.readLines()
        println("File contents: $lines")
        val kotlinVersion = lines.first { it.contains("kotlinJvmVersion") }.substringAfter("=")

        plugins {
            id("org.jetbrains.kotlin.jvm") version(kotlinVersion)
        }

    } else {
        println("File $fileName not found in any parent directories from ${startingDir.path}")
    }


}

gradle.lifecycle.beforeProject {
    // Allows root build to programmatically build all projects/builds at once
    apply(plugin = "base")
    println("""
        
        Before project info:
            Project name: ${project.name}
            Build tree path: ${project.buildTreePath}
            File: ${file(".")}
            
    """.trimIndent())

    /*
        Ensures repositories are globally configured the same.
        This includes any included builds, sub-projects, and the root build/root build.gradle.kts file.
     */

    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}
