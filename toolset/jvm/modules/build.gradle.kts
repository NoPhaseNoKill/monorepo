plugins {
    `kotlin-dsl`

    /*
        Configures the dependency analysis plugin for all subprojects (libraries/applications),
        which allows us to run './gradle check' that invokes the checkDependencyFormattingProject
        task.

        See: 'dependency-analysis-project' plugin for details
     */
    id("com.autonomousapps.dependency-analysis") version "1.29.0"
}

// tasks.register("checkFeatures") {
//     group = "verification"
//     description = "Run all feature tests"
//     dependsOn(gradle.includedBuild("admin-feature").task(":config:check"))
//     dependsOn(gradle.includedBuild("user-feature").task(":data:check"))
//     dependsOn(gradle.includedBuild("user-feature").task(":table:check"))
// }

dependencies {
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
}

tasks.register("testAll") {
    group = "verification"
    description = "Run all feature tests"

    dependsOn(tasks.test)
//     gradle.includedBuilds.forEach {
//         println("Included build for jvm: ${it.name}")
//     }
// //     val platformDependsOnAssemble = dependsOn(gradle.includedBuild("platform").task(":assemble"))
// //     platformDependsOnAssemble.dependsOn(gradle.includedBuild("plugins").task(":dependency-analysis-platform:check"))
// //
// //
// //     // dependsOn(gradle.includedBuild("modules").task(":check"))
// //
// //     // dependsOn("modules:libraries:list")
// //     // dependsOn("modules:libraries:utilities")
// //     // dependsOn("modules:applications:app")
// //
// //
// //     dependsOn(gradle.includedBuild("plugins"))
}



group = "com.nophasenokill.modules"