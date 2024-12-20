
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions

// Configures javadoc task for java projects, ensuring javadoc is compliant.
// Javadoc is generated for private classes and methods, and files are allowed to omit javadoc.
// These requirements are different than those of the public javadoc.
// This does not configure the public Javadoc published to the website

pluginManager.withPlugin("com.nophasenokill.code-quality") {
    tasks {
        named("codeQuality") {
            dependsOn(tasks.withType<Javadoc>())
        }
    }
}

tasks.withType<Javadoc>().configureEach {
    assert(name != "javadocAll") // This plugin should not be applied to the :docs project.

    onlyIf("Do not run the task if there are no java sources") {
        // Javadoc task will complain if we only have package-info.java files and no
        // other java files (as is with some Kotlin projects)
        !source.matching {
            exclude("**/package-info.java")
        }.isEmpty
    }

    options {
        this as StandardJavadocDocletOptions

        encoding = "utf-8"
        docEncoding = "utf-8"
        charSet = "utf-8"

        // Enable all javadoc warnings, except for:
        // - missing: Classes and methods are not required to have javadoc
        // - reference: We allow references to classes that are not part of the compilation
        addBooleanOption("Xdoclint:all,-missing,-reference", true)

        // Add support for custom tags
        tags("apiNote:a:API Note:", "implSpec:a:Implementation Requirements:", "implNote:a:Implementation Note:")

        // Process all source files for valid javadoc, not just public ones
        addBooleanOption("private", true)
        addBooleanOption("package", true)
    }
}
