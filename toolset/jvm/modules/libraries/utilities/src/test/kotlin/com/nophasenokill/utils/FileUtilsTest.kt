package com.nophasenokill.utils

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.pathString

class FileUtilsTest {

    @Test
    @Disabled
    fun `should rename directories recursively`() {
        // Example usage
        val startingDir = "/home/tomga/projects/gradle-project-setup-howto"
        val from = "org/example"
        val to = "com/nophasenokill"

        renameDirectoriesRecursively(startingDir, from, to)
    }

    private fun renameDirectoriesRecursively(startingDir: String, from: String, to: String) {
        val startingPath = Paths.get(startingDir)
        val fromPath = Paths.get(from)
        val toPath = Paths.get(to)

        Files.walkFileTree(startingPath, object : SimpleFileVisitor<Path>() {
            @Throws(IOException::class)
            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                if (dir.endsWith(fromPath)) {
                    val newDir = startingPath.resolve(dir.toString().replace(fromPath.toString(), toPath.toString()))
                    if(!File(newDir.pathString).exists()) {
                        File(newDir.pathString).mkdirs()
                    }
                    Files.move(dir, newDir, StandardCopyOption.REPLACE_EXISTING)

                    // Remove empty parent directories
                    var parent = dir.parent
                    while (parent != null && !parent.equals(startingPath)) {
                        if (parent.toFile().list()?.isEmpty() == true) {
                            Files.delete(parent)
                        }
                        parent = parent.parent
                    }

                    return FileVisitResult.SKIP_SUBTREE
                }
                return FileVisitResult.CONTINUE
            }
        })
    }
}