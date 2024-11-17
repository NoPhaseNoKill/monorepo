import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import com.gradle.develocity.agent.gradle.DevelocityPlugin
import org.gradle.kotlin.dsl.develocity

class BinaryRootSettingsPluginOne: Plugin<Settings> {
    override fun apply(settings: Settings) {
        settings.run {
            pluginManager.apply(DevelocityPlugin::class.java)

            pluginManager.withPlugin("com.gradle.develocity") {

                develocity {
                    buildScan {
                        publishing.onlyIf {
                            val hasFailures = it.buildResult.failures.isNotEmpty()
                            println("Decision to publish build scan was: ${hasFailures}.")
                            if (hasFailures) {
                                println("Failures were: ${it.buildResult.failures}")
                            }
                            hasFailures
                        }

                        uploadInBackground.set(false)
                        termsOfUseUrl.set("https://gradle.com/terms-of-service")
                        termsOfUseAgree.set("yes")

                        obfuscation {
                            username { OBFUSCATED_USERNAME }
                            ipAddresses { addresses -> addresses.map { _ -> OBFUSCATED_HOSTNAME } }
                            hostname { OBFUSCATED_IP_ADDRESS }
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val OBFUSCATED_USERNAME = "OBFUSCATED_USERNAME"
        const val OBFUSCATED_HOSTNAME = "OBFUSCATED_HOSTNAME"
        const val OBFUSCATED_IP_ADDRESS = "OBFUSCATED_IP_ADDRESS"
    }
}
