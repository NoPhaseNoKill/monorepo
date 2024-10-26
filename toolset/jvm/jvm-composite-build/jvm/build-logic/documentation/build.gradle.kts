plugins {
    id("com.nophasenokill.build-logic.kotlin-dsl-gradle-plugin")
    id("com.nophasenokill.build-logic.groovy-dsl-gradle-plugin")
}

description = "Provides a plugin to generate Gradle's DSL reference, User Manual and Javadocs"

dependencies {
    implementation("com.nophasenokill:basics")
    implementation("com.nophasenokill:module-identity")

    implementation(projects.buildUpdateUtils)

    implementation("com.github.javaparser:javaparser-core")
    implementation("com.google.guava:guava")
    implementation("com.uwyn:jhighlight") {
        exclude(module = "servlet-api")
    }
    implementation("com.vladsch.flexmark:flexmark-all")
    implementation("commons-lang:commons-lang")
    implementation("org.asciidoctor:asciidoctor-gradle-jvm")
    implementation("org.asciidoctor:asciidoctorj")
    implementation("org.asciidoctor:asciidoctorj-pdf")
    implementation("dev.adamko.dokkatoo:dokkatoo-plugin")
    implementation("org.jetbrains.dokka:dokka-core")

    testImplementation(gradleTestKit())
}

gradlePlugin {
    plugins {
        register("gradleDocumentation") {
            id = "com.nophasenokill.documentation"
            implementationClass = "com.nophasenokill.docs.GradleBuildDocumentationPlugin"
        }
    }
}
