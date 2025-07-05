import java.net.URI

repositories {
    mavenCentral()
    maven {
        name = "Jetbrains compose public repository"
        url = URI("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    maven {
        url = uri("https://repo.gradle.org/gradle/libs-releases")
    }

    google()
    gradlePluginPortal()
}
