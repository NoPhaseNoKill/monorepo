import java.nio.file.Paths
import kotlin.io.path.pathString

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

group = "com.nophasenokill"
version = "1.0-local-dev"

dependencies {
    implementation(gradleApi())
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

val printRelativeProjectDirPath = tasks.register("printRelativeProjectDirPath") {
    val settingsFilePathOne = project.rootProject.projectDir.absolutePath
    val rootPathOne = Paths.get(settingsFilePathOne)
    val projectPathOne = Paths.get(project.projectDir.absolutePath)
    val relativeProjectPathOne = rootPathOne.relativize(projectPathOne)

    // Ensure the parent directory exists (the first task in the whole tree after a clean will need this)
    val parentDirectory = projectPathOne.resolve("build/custom-task/print-relative-project-dir-path").toFile()
    parentDirectory.mkdirs()

    val outputFile = parentDirectory.resolve("output-file.txt")

    if (!outputFile.exists()) {
        outputFile.createNewFile()
    }

    outputFile.writeText(relativeProjectPathOne.pathString)
}

tasks.test {
    dependsOn(printRelativeProjectDirPath)
    useJUnitPlatform()

    mustRunAfter(printRelativeProjectDirPath)
}

tasks.named("buildAll") {
    dependsOn(tasks.build)
}

dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:2.0.21")
    // implementation("com.gradle.develocity:com.gradle.develocity.gradle.plugin:3.18.1")
    implementation("com.gradle:develocity-gradle-plugin:3.18.1")

    implementation("org.gradle.toolchains.foojay-resolver-convention:org.gradle.toolchains.foojay-resolver-convention.gradle.plugin:0.8.0")

}

group = "com.nophasenokill"

gradlePlugin {
    /*
        This is a classic example of gradle obfuscating/ruining all assumptions you have of a mental modal.

        Every single other place, including all documentation, uses 'name' explicitly for the project or
        root project context. So you make the assumption that name is basically off limits. Whoops! You'd
        be wrong! Gradle gotcha again!

        Due to the DSL (horrendous API's), all of a sudden when you're inside this specific function/
        lamda only - they choose to break this convention and decide that it actually takes the form of
        getName() from the plugin class implementation.

        I've given up trying to understand why they made things SO difficult and confusing - and instead
        this project serves as the exact reason why I started down this rabbit hole in the first place:
        to do things in an intuitive way, rather than the absolute disgrace gradle continually choose to
        repeat.

        Using this 'new'/aka unintuitive way will allow me to replace boilerplate significantly in the long run.

        I originally found this from a commit here:
        https://github.com/jjohannes/gradle-plugins-howto/commit/register-plugins#diff-7626f32fed777e8437fc644a1a3151de1f0232d474b1072bbee15ad76ba9a80a

        IMPORTANT: Notice the interchanging upper/lowercase. This is not an accident/typo.



        1. When the group is:
            group = "com.nophasenokill"

        2. When you have a file:
            src/main/kotlin/MySettingsPlugin.kt

        These are equivalent:

             plugins.create("mySettingsPlugin") {
                 id = "com.nophasenokill.build-logic.build-logic-convention-plugins-settings-binary-plugin-one.settings"
                 implementationClass = "com.nophasenokill.MySettingsPlugin"
             }

             plugins.create("MySettingsPlugin") {
                id = "${project.group}.build-logic.build-logic-convention-plugins-settings-binary-plugin-one.settings"
                implementationClass = "${project.group}.${name}"
            }
     */
    plugins.create("SafeGradlePlugin") {
        id = "${project.group}.build-logic.build-logic-convention-plugins-settings-binary-plugin-one.settings"
        implementationClass = "${project.group}.${name}"
    }
}
