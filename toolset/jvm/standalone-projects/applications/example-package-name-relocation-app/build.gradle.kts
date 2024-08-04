import me.lucko.jarrelocator.JarRelocator
import me.lucko.jarrelocator.Relocation
import java.util.jar.JarFile
import java.util.stream.Collectors

/*
    Showcases how we can relocate the package name of an application.
    The intention In future is that we will migrate this to the standard kotlin 
    plugin, which will enable us to not have to add com/nophasenokill
    package name nesting - as it will automatically transform them
    correctly for us - removing the need for unnecessary/duplicate folder
    hierarchies.
    
    Currently, when you run build followed by testRelocation tasks, you
    should get something like:
    
    > Task :standalone-projects:applications:some-relocation-test:testRelocation
        Sample JAR file created at: ${rootDir}\standalone-projects\applications\some-relocation-test\build\libs\sample-1.0-SNAPSHOT.jar
        Primary input file: ${rootDir}\standalone-projects\applications\some-relocation-test\build\libs\sample-1.0-SNAPSHOT.jar
        Output file: ${gradleHomeDir}\.gradle\caches\8.9\transforms\9336bff1a1b79fe0060edd4cc60f713c-c795024b-04e4-4d50-b2b1-a7d5aff996c0\transformed\sample-1.0-SNAPSHOT-relocated.jar
        com.nophasenokill -> relocated.com.nophasenokill
        Relocations: [me.lucko.jarrelocator.Relocation@d9c2800]
        Starting relocation process...
        Relocation process completed.
        Transformed files: [${gradleHomeDir}\.gradle\caches\8.9\transforms\9336bff1a1b79fe0060edd4cc60f713c\transformed\sample-1.0-SNAPSHOT-relocated.jar]
        Relocation successful: [relocated.com.nophasenokill]
        Class in relocated JAR: relocated/com/nophasenokill/SampleClass.class
 */

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("me.lucko:jar-relocator:1.7")
    }
}

plugins {
    id("com.nophasenokill.kotlin-application-plugin")
}

group = "com.nophasenokill"
version = "1.0-SNAPSHOT"

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    runtimeOnly("me.lucko:jar-relocator:1.7")
    implementation("org.ow2.asm:asm:9.7")  // Ensure using the latest ASM library
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

@CacheableTransform
abstract class ClassRelocator : TransformAction<ClassRelocator.Parameters> {
    interface Parameters : TransformParameters {
        @get:CompileClasspath
        val externalClasspath: ConfigurableFileCollection
        @get:Input
        val excludedPackage: Property<String>
    }

    @get:Classpath
    @get:InputArtifact
    abstract val primaryInput: Provider<FileSystemLocation>

    @get:CompileClasspath
    @get:InputArtifactDependencies
    abstract val dependencies: FileCollection

    override fun transform(outputs: TransformOutputs) {
        val primaryInputFile = primaryInput.get().asFile
        println("Primary input file: $primaryInputFile")
        if (parameters.externalClasspath.contains(primaryInputFile)) {
            outputs.file(primaryInput)
        } else {
            val baseName = primaryInputFile.name.substring(0, primaryInputFile.name.length - 4)
            val outputFile = outputs.file("$baseName-relocated.jar")
            println("Output file: $outputFile")
            relocateJar(primaryInputFile, outputFile)
        }
    }

    private fun relocateJar(input: File, output: File) {
        try {
            val relocatedPackages = (dependencies.flatMap { it.readPackages() } + input.readPackages()).toSet()
            val nonRelocatedPackages = parameters.externalClasspath.flatMap { it.readPackages() }
            val relocations = (relocatedPackages - nonRelocatedPackages).map { packageName ->
                val toPackage = "relocated.$packageName"
                println("$packageName -> $toPackage")
                Relocation(packageName, toPackage)
            }
            println("Relocations: $relocations")

            val jarRelocator = JarRelocator(input, output, relocations)
            println("Starting relocation process...")
            jarRelocator.run()
            println("Relocation process completed.")
        } catch (e: Exception) {
            println("Error during relocation: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    private fun File.readPackages(): Set<String> {
        try {
            JarFile(this).use { jarFile ->
                return jarFile.stream()
                    .filter { !it.isDirectory }
                    .filter { it.name.endsWith(".class") }
                    .map { entry ->
                        entry.name.substringBeforeLast('/').replace('/', '.')
                    }
                    .collect(Collectors.toSet())
            }
        } catch (e: Exception) {
            println("Error reading packages from file $this: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}

configurations.create("externalClasspath")

val usage = Attribute.of("usage", String::class.java)
val artifactType = Attribute.of("artifactType", String::class.java)

dependencies {
    registerTransform(ClassRelocator::class) {
        from.attribute(artifactType, "jar")
        to.attribute(artifactType, "relocated-classes")
        parameters {
            externalClasspath.from(configurations.getByName("externalClasspath"))
            excludedPackage.set("org.gradle.api")
        }
    }
}

dependencies {
    attributesSchema {
        attribute(usage)
    }
}

configurations.create("compile") {
    attributes.attribute(usage, "api")
}

tasks.register<Jar>("createSampleJar") {
    archiveBaseName.set("sample")
    from(sourceSets["main"].output)
}

tasks.register("testRelocation") {
    dependsOn("createSampleJar")

    doLast {
        val jarFile = file("${layout.buildDirectory.asFile.get().path}/libs/sample-1.0-SNAPSHOT.jar")
        println("Sample JAR file created at: $jarFile")

        val tempConfig = configurations.detachedConfiguration(
            dependencies.create(files(jarFile))
        ).apply {
            attributes {
                attribute(artifactType, "jar")
            }
        }

        val transformedFiles = tempConfig.incoming.artifactView {
            attributes {
                attribute(artifactType, "relocated-classes")
            }
        }.artifacts.artifactFiles

        println("Transformed files: ${transformedFiles.files}")
        assert(transformedFiles.files.size == 1) { "Expected one relocated file, but found ${transformedFiles.files.size}" }
        val relocatedFile = transformedFiles.singleFile

        // Verify the relocation
        val relocatedPackages = relocatedFile.readPackages()
        assert(relocatedPackages.contains("relocated.com.nophasenokill")) {
            "Relocation failed: $relocatedPackages"
        }
        println("Relocation successful: $relocatedPackages")

        // Display relocated classes
        JarFile(relocatedFile).use { jar ->
            jar.entries().asSequence()
                .filter { !it.isDirectory && it.name.endsWith(".class") }
                .forEach { entry ->
                    println("Class in relocated JAR: ${entry.name}")
                }
        }
    }
}


fun File.readPackages(): Set<String> {
    JarFile(this).use { jarFile ->
        return jarFile.entries().asSequence()
            .filter { !it.isDirectory }
            .filter { it.name.endsWith(".class") }
            .map { entry ->
                entry.name.substringBeforeLast('/').replace('/', '.')
            }
            .toSet()
    }
}
