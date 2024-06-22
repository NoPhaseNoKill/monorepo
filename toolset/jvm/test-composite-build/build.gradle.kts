    tasks.register("checkFeatures") {
        group = "verification"
        description = "Run all library and applications tests"

        // dependsOn(gradle.includedBuild("libraries").task(":library-dependant-on-single-library:check"))
        // dependsOn(gradle.includedBuild("libraries").task(":library-dependant-on-two-or-more-library:check"))
        // dependsOn(gradle.includedBuild("libraries").task(":library-with-no-library-dependencies:check"))
        //
        //
        // dependsOn(gradle.includedBuild("applications").task(":app-with-no-library-dependencies:check"))
        // dependsOn(gradle.includedBuild("applications").task(":app-with-single-library-dependencies:check"))
        // dependsOn(gradle.includedBuild("applications").task(":app-with-two-or-more-library-dependencies:check"))
    }