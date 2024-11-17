

repositories {
    maven {
        name = "Gradle public repository"
        url = uri("https://repo.gradle.org/gradle/public")
        content {
            includeGroup("net.rubygrapefruit")
            includeGroup("org.gradle.fileevents")
            includeModule("flot", "flot")
            includeModule("org.gradle", "gradle-tooling-api")
            includeModule("org.gradle.buildtool.internal", "configuration-cache-report")
            includeModule("org.gradle.buildtool.internal", "gradle-ide-starter")
        }
    }
    google {
        content {
            includeGroup("com.android.databinding")
            includeGroupByRegex("com\\.android\\.tools(\\.[a-z.\\-]*)?")
        }
    }
    maven {
        name = "CHAMP libs"
        url = uri("https://releases.usethesource.io/maven/")
        mavenContent {
            includeGroup("io.usethesource")
        }
    }
    mavenCentral()
}
