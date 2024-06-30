rootProject.name = "jvm"
//
// pluginManagement {
//     /*
//         The build-service-warning-plugin-fix-plugin is a required dep of root settings, and because
//         root-settings-plugins needs to be applied at root (inside of this file below), it specifically
//         needs to go here. This is due to the chicken/egg problem.
//      */
//     includeBuild("known-to-be-working-examples/plugins/root-settings-plugin")
//     includeBuild("known-to-be-working-examples/plugins/build-service-warning-fix-plugin")
//     includeBuild("known-to-be-working-examples/plugins/hash-source-plugin")
// }
//
//
// plugins {
//     id("com.nophasenokill.root-settings-plugin")
// }
//
// dependencyResolutionManagement {
//     rulesMode = RulesMode.FAIL_ON_PROJECT_RULES
// }

includeBuild("known-to-be-working-examples/plugins")
includeBuild("known-to-be-working-examples/domain")
includeBuild("known-to-be-working-examples/libraries")
includeBuild("known-to-be-working-examples/applications")