import com.nophasenokill.extensions.findCatalog
import com.nophasenokill.extensions.findCatalogVersion


tasks.register("checkJavaVersion") {

    fun locationVersionsMatch(): Boolean {
        val areSame =  System.getProperty("java.home") == System.getenv("JAVA_HOME")
        if(areSame) {
            return true
        }

        if(System.getProperty("java.home") !== null &&
            System.getProperty("java.home").contains(".sdkman") &&
            System.getenv("JAVA_HOME").endsWith("/current")
        ) {
            return true
        }

        return false
    }

    val file = layout.buildDirectory.file("java-version.txt")
    val buildDir = layout.buildDirectory.get().asFile
    val versionCatalog = project.findCatalog()
    val expectedJavaVersion = versionCatalog.findCatalogVersion("java")

    outputs.file(file)

    doFirst {

        if (!buildDir.exists()) {
            buildDir.mkdirs()
        }

        val expectedVersion = JavaVersion.toVersion(expectedJavaVersion)
        val isCurrent = JavaVersion.current() == expectedVersion
        val meetsRequirements = locationVersionsMatch() && isCurrent

        if(!meetsRequirements) {
            val message = """
                    There was a problem with your java config. This can lead to issues.
                    Please confirm:
                        - You have JDK ${expectedVersion} or higher. Current version is ${JavaVersion.current()}
                        - Your System.getProperty("java.home") is the same as your System.getenv("JAVA_HOME")
                          System.getProperty("java.home") is: ${System.getProperty("java.home")}
                          System.getenv("JAVA_HOME") is:  ${System.getenv("JAVA_HOME")}
                        These will eliminate strange side effects from IDE's or misconfigurations down the track
                """.trimIndent()
            throw GradleException(message)
        } else {
            println("Java version verified. Correctly set to: $expectedVersion")
            file.get().asFile.writeText("Version is: $expectedVersion")
        }
    }
}
