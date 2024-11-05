val implementation = configurations.create("implementation") {
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_API))
    }
}
val runtimeOnly = configurations.create("runtimeOnly") {
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
    }
}

val compileClasspath = configurations.create("compileClasspath") {
    extendsFrom(implementation)
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_API))
    }
}
val runtimeClasspath = configurations.create("runtimeClasspath") {
    extendsFrom(implementation, runtimeOnly)
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
    }
}

dependencies {
    implementation(project(":module"))
    implementation("org.slf4j:slf4j-api")
    runtimeOnly("org.slf4j:slf4j-simple")
}

val compileJava = tasks.register<JavaCompile>("compileJava") {
    destinationDirectory.set(layout.buildDirectory.dir("classes/java"))
    val javaSpecificFiles = layout.projectDirectory.dir("src").asFileTree.matching { include("**/*.java") }
    source(javaSpecificFiles)

    sourceCompatibility = "21"
    targetCompatibility = "21"

    classpath = files(compileClasspath)

    doLast {
        source.files.forEach { println("Source file included from: ${it.path}") }
        classpath.files.forEach { println("Classpath file included from: ${it.path}") }
        println("compileJava source compatibility: ${sourceCompatibility}, target compatibility: ${targetCompatibility}")
    }
}

tasks.register<JavaExec>("run") {
    mainClass.set("mypackage.App")
    classpath = files(compileJava.flatMap { it.destinationDirectory }, runtimeClasspath)
    doLast {
        classpath.files.forEach { println("Classpath file included from: ${it.path}") }
    }
}

tasks.register("build") {
    dependsOn(tasks.named("gatherBuildScriptDependencies"))
    dependsOn(tasks.named("gatherProjectDependencies"))

    dependsOn(compileJava)
}
