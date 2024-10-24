


pluginManagement {

    /*
        TODO See if we can somehow move this downwards

        Currently it appears as though this needs to be at the root level for some reason
        and cannot be moved downwards.

        Otherwise we get:

            settings.gradle.kts
            org.jetbrains.kotlin:kotlin-stdlib:{strictly 1.9.24} -> 1.9.24 constraint

     */
    // buildscript {
    //     configurations.all {
    //         resolutionStrategy {
    //             force("org.jetbrains.kotlin:kotlin-stdlib:2.1.0-Beta1")
    //         }
    //     }
    // }

    println("Hash code: ${settings.getBuildscript().hashCode()}")
    println("Hash code: ${buildscript.hashCode()}")
    // buildscript {
    //     configurations.classpath {
    //         resolutionStrategy {
    //             eachDependency {
    //                 if (this.requested.group == "org.jetbrains.kotlin") {
    //                     this.useVersion("2.1.0-Beta1")
    //                 }
    //             }
    //         }
    //     }
    // }


    // buildscript.configurations.all {
    //     println("Forced modules for ${this.name}: ${this.resolutionStrategy.forcedModules}")
    // }

    includeBuild("modules/meta-plugins")
}


rootProject.name = "jvm"


plugins {
    /*
        Must be published to $rootDir/local-repo before this can get resolved
     */
    id("com.nophasenokill.root-settings-plugin")

}



