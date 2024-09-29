plugins {
    kotlin("jvm") version "1.8.10"
    application
}

repositories {
    mavenCentral()
}

tasks.jar {
    manifest {
        attributes(
            "Premain-Class" to "com.nophasenokill.Agent"
        )
    }
}

tasks.withType<Test> {
    val agentJar = tasks.jar.get().archiveFile.get().asFile.absolutePath
    jvmArgs = listOf("-javaagent:$agentJar")
}

dependencies {
    implementation("org.ow2.asm:asm:9.7")
    implementation("org.ow2.asm:asm-commons:9.7")
    implementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.junit.platform:junit-platform-launcher:1.10.0")
}

// tasks.register("compileBuildHelper", JavaCompile::class.java).configure {
//     source = fileTree("buildhelper") {
//         include ("*.java")
//     }
//     classpath = files(configurations.buildhelper)
//     destinationDirectory.set(file("build/buildhelper"))
// }

// tasks.register("generateModuleInfo", JavaExec::class) {
//     group = "build"
//     description = "Generate module-info.class using ASM"
//     val outputDir = file("${buildDir}/classes/java/main")
//
//     doLast {
//         val cw = org.objectweb.asm.ClassWriter(0)
//         cw.visit(org.objectweb.asm.Opcodes.V9, org.objectweb.asm.Opcodes.ACC_MODULE, "module-info", null, null, null)
//         val mv = cw.visitModule("org.joml", 0, project.version.toString())
//         mv.visitRequire("java.base", org.objectweb.asm.Opcodes.ACC_MANDATED, "9")
//         mv.visitRequire("jdk.unsupported", org.objectweb.asm.Opcodes.ACC_STATIC_PHASE, null)
//         mv.visitRequire("jdk.incubator.vector", org.objectweb.asm.Opcodes.ACC_STATIC_PHASE, null)
//         mv.visitExport("org/joml", 0, null as Array<String>?)
//         mv.visitEnd()
//         cw.visitEnd()
//
//         file(outputDir).mkdirs()
//         FileOutputStream(File(outputDir, "module-info.class")).use { it.write(cw.toByteArray()) }
//     }
// }
//
// tasks.named("compileJava").configure {
//     finalizedBy("generateModuleInfo")
// }
