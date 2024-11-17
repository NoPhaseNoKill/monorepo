package gradlebuild.basics.classanalysis

import org.gradle.api.attributes.Attribute
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.commons.ClassRemapper
import org.objectweb.asm.commons.Remapper
import java.io.*
import java.net.URI
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

private val ignoredPackagePatterns = PackagePatterns(setOf("java"))

object Attributes {
    val artifactType = Attribute.of("artifactType", String::class.java)
    val minified = Attribute.of("minified", Boolean::class.javaObjectType)
}

class JarAnalyzer(
    private val shadowPackage: String,
    private val keepPackages: Set<String>,
    private val unshadedPackages: Set<String>,
    private val ignorePackages: Set<String>,
) {
    private val packagePatterns = PackagePatterns(ignorePackages)

    fun analyze(jarFile: File, classesDir: File, manifestFile: File, buildReceipt: File): ClassGraph {
        val classGraph = createClassGraph()

        URI.create("jar:${jarFile.toPath().toUri()}").let { jarUri ->
            FileSystems.newFileSystem(jarUri, emptyMap<String, Any>()).use { jarFileSystem ->
                jarFileSystem.rootDirectories.forEach { rootDir ->
                    visitClassDirectory(rootDir, classGraph, classesDir, manifestFile.toPath(), buildReceipt.toPath())
                }
            }
        }
        return classGraph
    }

    private fun createClassGraph() =
        ClassGraph(
            PackagePatterns(keepPackages),
            PackagePatterns(unshadedPackages),
            packagePatterns,
            shadowPackage
        )

    private fun visitClassDirectory(dir: Path, classes: ClassGraph, classesDir: File, manifest: Path, buildReceipt: Path) {
        Files.walkFileTree(dir, object : SimpleFileVisitor<Path>() {

            private var seenManifest = false

            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                when {
                    file.isClassFile() -> processClassFile(file, classes, classesDir)
                    file.isBuildReceipt() -> Files.copy(file, buildReceipt)
                    file.isUnseenManifestFile() -> {
                        seenManifest = true
                        Files.copy(file, manifest)
                    }
                }
                return FileVisitResult.CONTINUE
            }

            private fun Path.isClassFile() = toString().endsWith(".class")
            private fun Path.isBuildReceipt() = toString() == "/org/gradle/build-receipt.properties"
            private fun Path.isUnseenManifestFile() = toString() == "/${JarFile.MANIFEST_NAME}" && !seenManifest

            private fun processClassFile(file: Path, classes: ClassGraph, classesDir: File) {
                Files.newInputStream(file).use { inputStream ->
                    val reader = ClassReader(inputStream)
                    val details = classes[reader.className]
                    details.visited = true
                    val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
                    reader.accept(createClassRemapper(classWriter, classes, details), ClassReader.EXPAND_FRAMES)
                    classesDir.resolve(details.outputClassFilename).apply {
                        parentFile.mkdirs()
                        writeBytes(classWriter.toByteArray())
                    }
                }
            }

            private fun createClassRemapper(classWriter: ClassWriter, classes: ClassGraph, details: ClassDetails) =
                ClassRemapper(classWriter, object : Remapper() {
                    override fun map(name: String): String {
                        return when {
                            ignoredPackagePatterns.matches(name) -> name
                            else -> {
                                val dependencyDetails = classes[name]
                                if (dependencyDetails !== details) {
                                    details.dependencies.add(dependencyDetails)
                                }
                                dependencyDetails.outputClassName
                            }
                        }
                    }
                })
        })
    }
}

object ZipEntryConstants {
    val CONSTANT_TIME_FOR_ZIP_ENTRIES: Long =
        LocalDateTime.of(1980, 2, 1, 0, 0).toEpochSecond(ZoneOffset.ofHours(1)) * 1000
}

fun JarOutputStream.addJarEntry(entryName: String, sourceFile: File) {
    val entry = ZipEntry(entryName).apply { time = ZipEntryConstants.CONSTANT_TIME_FOR_ZIP_ENTRIES }
    putNextEntry(entry)
    BufferedInputStream(FileInputStream(sourceFile)).use { it.copyTo(this) }
    closeEntry()
}

fun File.getClassSuperTypes(): Set<String> {
    if (!path.endsWith(".class")) throw IllegalArgumentException("Not a class file: $path")
    inputStream().use { return ClassReader(it).run { setOf(superName) + interfaces } }
}
