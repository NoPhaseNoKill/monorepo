import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.gradle.plugins.ide.idea.model.Module
import org.gradle.plugins.ide.idea.model.ModuleDependency
import org.gradle.plugins.ide.idea.model.Project
import org.w3c.dom.Element

plugins {
    id("java")
    idea
    kotlin("jvm") version("2.1.0-Beta1")
}

idea {

    module.iml {
        beforeMerged(Action<Module> {
            dependencies.clear()
        })
    }

    module.iml {
        whenMerged(Action<Module> {
            dependencies.forEach {
                (it as ModuleDependency).isExported = true
            }
        })
    }

    project.ipr {
        beforeMerged(Action<Project> {
            modulePaths.clear()
        })
    }

    project.ipr {
        withXml(Action<XmlProvider> {
            fun Element.firstElement(predicate: (Element.() -> Boolean)) =
                childNodes
                    .run { (0 until length).map(::item) }
                    .filterIsInstance<Element>()
                    .first { it.predicate() }

            asElement()
                .firstElement { tagName == "component" && getAttribute("name") == "VcsDirectoryMappings" }
                .firstElement { tagName == "mapping" }
                .setAttribute("vcs", "Git")
        })
    }

    module {
        sourceDirs = files(file("src/java"), file("src/kotlin")).toSet()
        generatedSourceDirs =  files(file("classes/java"), file("classes/kotlin")).toSet()
        targetBytecodeVersion = JavaVersion.VERSION_21
        languageLevel = IdeaLanguageLevel(JavaVersion.VERSION_21)
    }
}

java.sourceSets["main"].java {
    srcDirs("src/java", "src/kotlin")
}

val implementation = configurations.named("implementation").get()
val api = configurations.named("api").get()
val runtimeOnly = configurations.named("runtimeOnly").get()
val compileClasspath = configurations.named("compileClasspath") {
    extendsFrom(implementation, api)
}
dependencies {
    api("jakarta.activation:jakarta.activation-api:2.1.3")
    implementation("commons-io:commons-io:2.17.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1")
}

tasks.named("build") {
    dependsOn(tasks.named("compileKotlin"), tasks.named("compileJava"))
}
