

plugins {
    `maven-publish`
    `kotlin-dsl`
}

group = "com.nophasenokill"
version = "1.0.0-local-dev"

gradlePlugin {
    plugins {
        create("publishing-plugin-one") {
            id = "com.nophasenokill.publishing-plugin-one"
            implementationClass = "com.nophasenokill.PublishingPluginOne"
        }
    }
}

java {
    /*
        Configures an output jar containing the source code
        as opposed to just the compiled code. This helps
        developers read/find source code more easily.

        This method under the hood adds a new configuration,
        and hence also adds to the attribute of docstype.

        This docstype is used for variant selection and an
        example can be seen here:

            Variant sourcesElements
            --------------------------------------------------
            sources elements for main.

            Capabilities
                - com.nophasenokill:publishing-plugin-one:1.0.0-local-dev (default capability)
            Attributes
                - org.gradle.category            = documentation
                - org.gradle.dependency.bundling = external
                - org.gradle.docstype            = sources
                - org.gradle.usage               = java-runtime
            Artifacts
                - build/libs/publishing-plugin-one-1.0.0-local-dev-sources.jar (artifactType = jar, classifier = sources)

     */
    withSourcesJar()
}

publishing {
    repositories {
        maven {
            url = uri("${rootProject.projectDir}/local-repo")

            /*
                Because we control all the modules we publish, we can assert
                that they always contain gradle metadata. By adding the below,
                we now get optimized network calls.

                See: https://docs.gradle.org/8.8/userguide/publishing_gradle_module_metadata.html#sub:interactions-other-build-tools
             */
            metadataSources {
                gradleMetadata()
            }
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}
val javaComponent = components["java"] as AdhocComponentWithVariants

javaComponent.withVariantsFromConfiguration(configurations["sourcesElements"]) {
    skip()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

// class InstrumentedJarsPlugin @Inject constructor(
//
//     private val softwareComponentFactory: SoftwareComponentFactory
//
// ) : Plugin<Project> {
//
//     override fun apply(target: Project) {
//         // create an adhoc component
//         val adhocComponent = softwareComponentFactory.adhoc("myAdhocComponent")
//
//         // add it to the list of components that this project declares
//         components.add(adhocComponent)
//
//         // and register a variant for publication
//         adhocComponent.addVariantsFromConfiguration(outgoing) {
//             mapToMavenScope("runtime")
//         }
//     }
//
// }
