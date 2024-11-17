import com.nophasenokill.basics.capitalize
import com.google.gson.Gson
import java.net.URI

wrapperUpdateTask("nightly", "nightly")
wrapperUpdateTask("rc", "release-candidate")
wrapperUpdateTask("current", "current")

tasks.withType<Wrapper>().configureEach {
    val jvmOpts = "-Dfile.encoding=UTF-8"
    inputs.property("jvmOpts", jvmOpts)
    doLast {
        val optsEnvVar = "DEFAULT_JVM_OPTS"
        scriptFile.writeText(scriptFile.readText().replace("$optsEnvVar='", "$optsEnvVar='$jvmOpts "))
        batchScript.writeText(batchScript.readText().replace("set $optsEnvVar=", "set $optsEnvVar=$jvmOpts "))
    }
}

fun Project.wrapperUpdateTask(name: String, label: String) {
    val wrapperTaskName = "${name}Wrapper"
    val configureWrapperTaskName = "configure${wrapperTaskName.capitalize()}"

    val wrapperTask = tasks.register<Wrapper>(wrapperTaskName) {
        dependsOn(configureWrapperTaskName)
        group = "wrapper"
    }

    tasks.register(configureWrapperTaskName) {

        val jsonText = URI("https://services.gradle.org/versions/$label").toURL().readText()
        val versionInfo = Gson().fromJson(jsonText, VersionDownloadInfo::class.java)
        val task = wrapperTask.get()
        task.distributionUrl = versionInfo.downloadUrl
    }
}

data class VersionDownloadInfo(val version: String, val downloadUrl: String)
