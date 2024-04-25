plugins {
    kotlin("jvm") version "1.9.23"
    `java-gradle-plugin`
    publishing
}

group = "meta-plugins"

repositories {
    mavenCentral()
}

/*
    Remaining dependency we can't touch is: kotlinCompilerClasspath which appears to have a direct
    need for reflect 1.6.10

    kotlinCompilerClasspath
    \--- org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.23
     +--- org.jetbrains.kotlin:kotlin-stdlib:1.9.23
     |    \--- org.jetbrains:annotations:13.0
     +--- org.jetbrains.kotlin:kotlin-script-runtime:1.9.23
     +--- org.jetbrains.kotlin:kotlin-reflect:1.6.10
 */
project.pluginManager.withPlugin("kotlin") {
    val classpathConfiguration = requireNotNull(configurations.findByName("kotlinBuildToolsApiClasspath"))
    val klibCommonizerConfiguration = requireNotNull(configurations.findByName("kotlinKlibCommonizerClasspath"))

    classpathConfiguration.dependencies.add(project.dependencies.add("kotlinBuildToolsApiClasspath", "org.jetbrains.kotlin:kotlin-reflect:1.9.23"))
    klibCommonizerConfiguration.dependencies.add(project.dependencies.add("kotlinKlibCommonizerClasspath", "org.jetbrains.kotlin:kotlin-reflect:1.9.23"))
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.23")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.23"))

}

gradlePlugin {
    plugins {
        create("meta-plugin-one") {
            id = "com.nophasenokill.meta-plugins.meta-plugin-one"
            implementationClass = "com.nophasenokill.MetaPluginOne"
        }
    }
}
