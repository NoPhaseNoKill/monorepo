
plugins {
    alias(libs.plugins.kotlinDsl)
    publishing
    `maven-publish`
}

group = "com.nophasenokill.$name"
version = "0.1.local-dev"

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            /*
                 Configures the resolution in each of the dependencies to be the same so we don't get misconfigurations
                 when publishing
              */
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
        }

        repositories {
            maven {
                url = uri(isolated.rootProject.projectDirectory.dir("build/local-repos"))
            }
        }
    }
}

gradlePlugin {
    plugins {
        create("componentPlugin") {
            id = "com.nophasenokill.component-plugin"
            implementationClass = "com.nophasenokill.ComponentPlugin"
        }
    }
}


dependencies {
    implementation("com.gradle:develocity-gradle-plugin:${libs.versions.gradleEnterprise.get()}")
    implementation("org.gradle.toolchains:foojay-resolver:${libs.versions.fooJayResolver.get()}")
}
