plugins {
    id("com.nophasenokill.java-plugin")
}


repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        url = uri("${rootProject.projectDir}/local-published-plugins")
    }
}
