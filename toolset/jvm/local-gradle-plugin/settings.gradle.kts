import java.io.File
import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.provider.Property
import org.gradle.process.ExecOperations


println("About to sleep")

plugins.apply(SoundFeedbackPlugin::class.java)

class SoundFeedbackPlugin @Inject constructor(
    private val flowScope: FlowScope,
    private val flowProviders: FlowProviders,
) : Plugin<Settings> {

    override fun apply(target: Settings) {
        val successDir = File(target.settingsDir, "sounds/success")
        val failureDir = File(target.settingsDir, "sounds/failure")
        val supportedExtensions = listOf("mp3")

        successDir.mkdirs()
        failureDir.mkdirs()

        if (!successDir.exists() || !failureDir.exists()) {
            throw GradleException("Sound directories not found: Ensure that $successDir and $failureDir directories exist and contain audio files.")
        }

        fun getRandomSoundFile(dir: File): File {
            val soundFiles = dir.listFiles { file -> file.isFile && supportedExtensions.contains(file.extension) }
                ?.toList() ?: emptyList()

            if (soundFiles.isEmpty()) {
                throw GradleException("No valid sound files found in directory: $dir")
            }
            return soundFiles.random()
        }

        flowScope.always(
            FFPlay::class.java
        ) {
            parameters.mediaFile.set(
                flowProviders.buildWorkResult.map { result ->
                    if (result.failure.isPresent) getRandomSoundFile(failureDir) else getRandomSoundFile(successDir)
                }
            )
        }
    }
}

class FFPlay @Inject constructor(
    private val execOperations: ExecOperations
) : FlowAction<FFPlay.Parameters> {

    interface Parameters : FlowParameters {
        @get:Input
        val mediaFile: Property<File>
    }

    override fun execute(parameters: Parameters) {
        val mediaFile = parameters.mediaFile.get()

        println("Playing sound: ${mediaFile.absolutePath}")
        execOperations.exec {
            commandLine("ffplay", "-nodisp", "-autoexit", "-hide_banner", "-loglevel", "quiet", mediaFile.absolutePath)
            isIgnoreExitValue = true
        }
    }
}

Thread.sleep(1000)
println("About to run again")

include("plugin")
