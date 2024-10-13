
plugins {
    // id("com.gradle.develocity") version("3.18.1")
}

val OBFUSCATED_USERNAME = "OBFUSCATED_USERNAME"
val OBFUSCATED_HOSTNAME = "OBFUSCATED_HOSTNAME"
val OBFUSCATED_IP_ADDRESS = "OBFUSCATED_IP_ADDRESS"

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
