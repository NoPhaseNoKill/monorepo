// import com.nophasenokill.ProjectType
// import com.nophasenokill.mySettings


rootProject.name = "jvm"

pluginManagement {

    plugins {
        id("com.gradle.develocity") version "3.18.1"
    }
}

includeBuild("test-build-logic")
includeBuild("consumer")
includeBuild("producer")

gradle.lifecycle.beforeProject {
    tasks.register("build") {
        dependsOn(gradle.includedBuilds.map { it.task(":build") })
    }
}

// pluginManagement {
//     includeBuild("build-logic")
// }
//
// /*
//     Applies to the root settings
//  */
// plugins {
//     id("com.nophasenokill.build-logic.build-logic-convention-plugins-settings-convention-plugin-one")
//     id("com.nophasenokill.build-logic.build-logic-convention-plugins-settings-binary-plugin-one.settings")
// }
//
// mySettings {
//     applyCustomLogic.set(true)
//
//     includeCustomProject("root-one-sub-project-one", ProjectType.ROOT_ONE, "root-one")
//     includeCustomProject("root-two-sub-project-one", ProjectType.ROOT_TWO, "root-two")
// }

plugins {
    id("com.gradle.develocity")
}

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
            username { "OBFUSCATED_USERNAME" }
            ipAddresses { addresses -> addresses.map { _ -> "OBFUSCATED_HOSTNAME" } }
            hostname { "OBFUSCATED_IP_ADDRESS" }
        }
    }
}
