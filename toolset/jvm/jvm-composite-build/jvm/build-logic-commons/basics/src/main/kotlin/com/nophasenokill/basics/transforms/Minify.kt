

package com.nophasenokill.basics.transforms

import com.google.common.io.Files
import com.nophasenokill.basics.classanalysis.ClassAnalysisException
import com.nophasenokill.basics.classanalysis.ClassDetails
import com.nophasenokill.basics.classanalysis.ClassGraph
import com.nophasenokill.basics.classanalysis.JarAnalyzer
import com.nophasenokill.basics.classanalysis.addJarEntry
import org.gradle.api.artifacts.transform.CacheableTransform
import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarFile
import java.util.jar.JarOutputStream


@CacheableTransform
abstract class Minify : TransformAction<Minify.Parameters> {

    interface Parameters : TransformParameters {
        @get:Input
        var keepClassesByArtifact: Map<String, Set<String>>
    }

    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    @get:InputArtifact
    abstract val artifact: Provider<FileSystemLocation>

    override fun transform(outputs: TransformOutputs) {
        for (entry in parameters.keepClassesByArtifact) {
            val fileName = artifact.get().asFile.name
            if (fileName.startsWith(entry.key)) {
                val nameWithoutExtension = Files.getNameWithoutExtension(fileName)
                minify(artifact.get().asFile, entry.value, outputs.file("$nameWithoutExtension-min.jar"))
                return
            }
        }
        outputs.file(artifact)
    }

    private
    fun minify(artifact: File, keepClasses: Set<String>, jarFile: File): File {
        val tempDirectory = java.nio.file.Files.createTempDirectory(jarFile.name).toFile()
        val classesDir = tempDirectory.resolve("classes")
        val manifestFile = tempDirectory.resolve("MANIFEST.MF")
        val buildReceiptFile = tempDirectory.resolve("build-receipt.properties")
        val classGraph = JarAnalyzer("", keepClasses, keepClasses, setOf()).analyze(artifact, classesDir, manifestFile, buildReceiptFile)

        createJar(classGraph, classesDir, manifestFile, jarFile)

        return jarFile
    }

    private
    fun createJar(classGraph: ClassGraph, classesDir: File, manifestFile: File, jarFile: File) {
        try {
            JarOutputStream(BufferedOutputStream(FileOutputStream(jarFile))).use { jarOutputStream ->
                if (manifestFile.exists()) {
                    jarOutputStream.addJarEntry(JarFile.MANIFEST_NAME, manifestFile)
                }
                val visited = linkedSetOf<ClassDetails>()
                for (classDetails in classGraph.entryPoints) {
                    visitTree(classDetails, classesDir, jarOutputStream, visited)
                }
            }
        } catch (exception: Exception) {
            throw ClassAnalysisException("Could not write shaded Jar $jarFile", exception)
        }
    }

    private
    fun visitTree(
        classDetails: ClassDetails,
        classesDir: File,
        jarOutputStream: JarOutputStream,
        visited: MutableSet<ClassDetails>
    ) {

        if (!visited.add(classDetails)) {
            return
        }
        if (classDetails.visited) {
            val fileName = classDetails.outputClassFilename
            val classFile = classesDir.resolve(fileName)
            jarOutputStream.addJarEntry(fileName, classFile)
            for (dependency in classDetails.dependencies) {
                visitTree(dependency, classesDir, jarOutputStream, visited)
            }
        }
    }
}
