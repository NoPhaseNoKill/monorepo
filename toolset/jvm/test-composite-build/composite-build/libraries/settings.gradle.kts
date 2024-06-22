rootProject.name = "libraries"

/*

    KEY TAKEAWAYS:

        1. Always include any required includedBuilds at the absolute
        closest settings file to each of the builds. This would mean,
        for instance, that if we had two includedBuilds that were
        for libraries and applications respectively, any includedBuilds
        which added binary plugins for these, SHOULD be included
        in their respective settings.gradle.kts file.

        2. Because the majority of the plugins (at this current time) are in
        the one includedBuild - this would then mean that the
        settings for libraries/applications SHOULD always be:

            // NOTICE THE VERY CAREFUL ORDERING OF THESE
            rootProject.name = "libraries" // or applications

            includeBuild("composite-build/standalone-plugin") // has no reliance on anything else, so goes first
            includeBuild("composite-build/meta-plugin") // has reliance on standalone-plugin so goes after that
            includeBuild("composite-build/standard-plugins") // has reliance on meta-plugin, which implicitly means
                                                             // it also has a reliance on standalone-plugin too
                                                             // and hence needs to go last

         Consider the following example:

            1. Plugin one realises dependency version 1.9.20 for gradle plugin and also realises plugin two

            2. Plugin two realises dependency version 1.9.21 for gradle plugin, as one of its transitive dependencies
            is pulled in. Plugin two realises dependency on plugin three

            3. Plugin three releases dependency version 1.9.22 for gradle plugin as one of its transitive dependencies
            is pulled in

        We've now just wasted time downloading 1.9.20, 1.9.21 - for the final build to override those values, and
        say actually I want you to use 1.9.22.


    REASONING:

        Notice that inside of the whole test-composite-build, the build
        will still pass if we do NOT include any of the standalone/
        standard/meta plugins builds.

        HOWEVER, if we open the libraries module by itself, without
        this, we will get error stating it can't find of plugins:

            Plugin [id: 'com.nophasenokill.base-kotlin-plugin'] was not found in any of the following sources:

        This means that if you ever are removing any of the includedBuilds
        from this file, to ensure that it works correctly - you need to run
        the following checks:

            1. First check:
                If structure is like:

                    libraries/settings.gradle.kts
                    libraries/consumer-kotlin-plugin/build.gradle.kts
                        -> need to open libraries by itself to confirm working

                    libraries
                    libraries/consumer-kotlin-plugin/settings.gradle.kts
                    libraries/consumer-kotlin-plugin/build.gradle.kts
                        -> need to open libraries/consumer-kotlin-plugin by itself to confirm working

            2. Second check
                -Check the whole build of test-composite-build (ie ./gradlew build)


        Both of these need to succeed, otherwise you have broken something.

        ***IF YOU NEED TO KNOW WHY***

            Why it works for the current composite-build WITHOUT having
            the includedBuild, but fails on the libraries module by itself:

            Because we rely on the external artifact dependency inside
            each of the library builds files,

                ie:
                    dependencies {
                        implementation("com.nophasenokill:base-kotlin-plugin")
                    }

                 it means (implicitly) that the root project has already included these
                 from the root build settings file where it does:
                    rootProject.name = "test-composite-build"
                    includeBuild("composite-build/standard-plugins")

                Because the libraries build is also being included, ie:

                    rootProject.name = "test-composite-build"

                    includeBuild("composite-build/standard-plugins")
                    includeBuild("composite-build/libraries")

                it means that we inherit this for free.

                VERY IMPORTANT FURTHER NOTE: We will only inherit this for free
                IF the includedBuild of the standard-plugins is BEFORE libraries

                Example:
                    WORKING EXAMPLE
                        rootProject.name = "test-composite-build"
                        includeBuild("composite-build/standard-plugins")
                        includeBuild("composite-build/libraries")

                    BROKEN EXAMPLE - Plugin [id: 'com.nophasenokill.base-kotlin-plugin'] was not found in any of the following sources:

                        rootProject.name = "test-composite-build"
                        includeBuild("composite-build/libraries")
                        includeBuild("composite-build/standard-plugins")

                    In this example, we COULD resolve the error if we then added the includedBuild
                    back into libraries (this file) - which would make it completely order independent.
                    This then means, that really, we should just always do this - making our builds
                    completely 'includedBuild order independent' - which results in a reproducible build.
 */
include("consumer-kotlin-plugin")
includeBuild("../standard-plugins")