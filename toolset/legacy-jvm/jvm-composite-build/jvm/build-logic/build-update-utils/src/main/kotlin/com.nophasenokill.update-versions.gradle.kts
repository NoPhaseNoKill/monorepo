
import com.google.gson.Gson
import com.nophasenokill.basics.releasedVersionsFile
import com.nophasenokill.buildutils.model.ReleasedVersion
import com.nophasenokill.buildutils.tasks.UpdateAgpVersions
import com.nophasenokill.buildutils.tasks.UpdateKotlinVersions
import com.nophasenokill.buildutils.tasks.UpdateReleasedVersions
import java.net.URI


tasks.named<UpdateDaemonJvm>("updateDaemonJvm") {
    jvmVersion = JavaLanguageVersion.of(21)
    jvmVendor = "adoptium"
}

tasks.withType<UpdateReleasedVersions>().configureEach {
    releasedVersionsFile = releasedVersionsFile()
    group = "Versioning"
}

tasks.register<UpdateReleasedVersions>("updateReleasedVersions") {
    currentReleasedVersion = ReleasedVersion(
        project.findProperty("currentReleasedVersion").toString(),
        project.findProperty("currentReleasedVersionBuildTimestamp").toString()
    )
}

tasks.register<UpdateReleasedVersions>("updateReleasedVersionsToLatestNightly") {
    currentReleasedVersion = project.provider {
        val jsonText = URI("https://services.gradle.org/versions/nightly").toURL().readText()
        println(jsonText)
        val versionInfo = Gson().fromJson(jsonText, VersionBuildTimeInfo::class.java)
        ReleasedVersion(versionInfo.version, versionInfo.buildTime)
    }
}

tasks.register<UpdateAgpVersions>("updateAgpVersions") {
    comment = " Generated - Update by running `./gradlew updateAgpVersions`"
    minimumSupportedMinor = "7.3"
    propertiesFile = layout.projectDirectory.file("gradle/dependency-management/agp-versions.properties")
    compatibilityDocFile = layout.projectDirectory.file("platforms/documentation/docs/src/docs/userguide/releases/compatibility.adoc")
}

tasks.register<UpdateKotlinVersions>("updateKotlinVersions") {
    comment = " Generated - Update by running `./gradlew updateKotlinVersions`"
    minimumSupported = "1.6.10"
    propertiesFile = layout.projectDirectory.file("gradle/dependency-management/kotlin-versions.properties")
    compatibilityDocFile = layout.projectDirectory.file("platforms/documentation/docs/src/docs/userguide/releases/compatibility.adoc")
}

data class VersionBuildTimeInfo(val version: String, val buildTime: String)
