## Plugin structure

### Overview

There are three main sections to the plugins: a segment plugin, a module plugin, and a root plugin.

The naming of these have been chosen carefully whereby a plugin is either module or root, and can be comprised of segments.

#### Segment - The foundations for exposed plugins
Building blocks for composing a plugin which requires multiple pieces of re-usable logic. Intended
use is for things like:

1. Defining java version
2. Defining the test log output levels

Composition of segments is encouraged, so that build cache invalidation can be kept to a minimum. This means
creating a segment which is as small as possible allows for as much re-use of a cache when a part of it
becomes invalid. Please see example below for more details on how and why to think about this.

#### Module - Exposed plugin specific to a project
A plugin to be used at the level of a module (aka project specific). Intended use is for things like:

1. A plugin specifically for java applications ('java-application-plugin')
2. A plugin specifically for kotlin libraries ('kotlin-library-plugin')

#### Root - Exposed plugin specific to all projects
A plugin to be used at the root level of the whole repository. This could be for either a
build.gradle.kts or settings.gradle.kts file at the root level. Intended use is for things like:

1. Dependency analysers (https://github.com/autonomousapps/dependency-analysis-gradle-plugin)
2. Toolchain resolvers (https://github.com/gradle/foojay-toolchains)

#### Real world example - Primary benefits

1. Re-use shared code
2. When cache is invalidated, due to say an update to test-logging-plugin, the cache being invalidated is kept
    to a bare minimum, which prevents unnecessary processing from gradle that often cause build times to grow large
3. This would mean in the example below, instead of three separate parts of the cache being invalidated if we decide
    to update the test-logging, only one part is invalidated.

#### Real world example - Example

You come into a gradle project, which looks something like:

```
    // multi-project
    projectRoot/someApplicationOne
    projectRoot/someApplicationTwo
    projectRoot/someLibraryOne 


    // projectRoot/settings.gradle.kts
    
    rootProject.name = "awesome-large-project"
    
    includeBuild("someApplicationOne")
    includeBuild("someApplicationTwo")
    includeBuild("someLibraryOne")

```

```
    // projectRoot/build.gradle.kts
    
    tasks.register("runAllTests") {
        group = "verification"
        description = "Run all feature tests"
        dependsOn(gradle.includedBuild("someApplicationOne").task(":check"))
        dependsOn(gradle.includedBuild("someApplicationTwo").task(":check"))
        dependsOn(gradle.includedBuild("someLibraryOne").task(":check"))
    }

```

```
    // shared code inside of each of the following:
    //    - projectRoot/someApplicationOne/build.gradle.kts
    //    - projectRoot/someApplicationTwo/build.gradle.kts
    //    - projectRoot/someLibraryOne/build.gradle.kts

    ...other setup code
        
    tasks.test {
        useJUnitPlatform()
        testLogging {
            events = setOf(
                TestLogEvent.FAILED,
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
            )
        }
    }
    
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}

```

So how do you begin to to de-duplicate this logic? It seems wasteful having
to declare it in three separate, independent files. And what happens if you forget
to update one of the files?

How you might structure this instead:

```
    // projectRoot/settings.gradle.kts
    
    // where we will define our 're-usable' plugins
    pluginManagement {
        repositories.gradlePluginPortal()
        includeBuild("build-logic") // allows the plugins to be 'discoverable' later
    }
    
    rootProject.name = "awesome-large-project"
    
    includeBuild("someApplicationOne")
    includeBuild("someApplicationTwo")
    includeBuild("someLibraryOne")

```

```
    // projectRoot/build.gradle.kts
    
    tasks.register("runAllTests") {
        group = "verification"
        description = "Run all feature tests"
        
        // notice the subtle difference here, specifically because we want a folder
        // inside of the build-logic to represent our plugins. You may not want to
        // depend on check, but another specific task - this is merely an example
        dependsOn(gradle.includedBuild("build-logic").task(":plugins:check")
        
        dependsOn(gradle.includedBuild("someApplicationOne").task(":check"))
        dependsOn(gradle.includedBuild("someApplicationTwo").task(":check"))
        dependsOn(gradle.includedBuild("someLibraryOne").task(":check"))
    }

```

```
    // projectRoot/build-logic/settings.gradle.kts

    rootProject.name = "build-logic"
    
    dependencyResolutionManagement {
        repositories.gradlePluginPortal()
    }
    
    include("plugins")

```

```
    // projectRoot/build-logic/plugins/build.gradle.kts

    plugins {
        `kotlin-dsl`
    }

```

```

    // PLEASE NOTE: The path here can be whatever you want, but it WILL impact how the plugin
    // is imported in each of someApplicationOne, someApplicationTwo, someLibraryOne
    
    // projectRoot/build-logic/plugins/src/main/kotlin/awesomeLargeProject/plugin/segment/java-version-plugin.gradle.kts
    
    plugins {
        id("java")
    }
    
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

```

```

    // PLEASE NOTE: The path here can be whatever you want, but it WILL impact how the plugin
    // is imported in each of someApplicationOne, someApplicationTwo, someLibraryOne
    
    // projectRoot/build-logic/plugins/src/main/kotlin/awesomeLargeProject/plugin/segment/test-logging-plugin.gradle.kts
    
    plugins {
        id("java")
    }
    
    tasks.test {
        useJUnitPlatform()
        testLogging {
            events = setOf(
                TestLogEvent.FAILED,
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
            )
        }
    }

```

```

    // PLEASE NOTE: The path here can be whatever you want, but it WILL impact how the plugin
    // is imported in each of someApplicationOne, someApplicationTwo, someLibraryOne
    
    // projectRoot/build-logic/plugins/src/main/kotlin/awesomeLargeProject/plugin/module/basic-java-project-plugin.gradle.kts
    
    plugins {
            id("awesomeLargeProject.plugin.segment.java-version-plugin") <----------- notice how the ids here are based
            id("awesomeLargeProject.plugin.segment.test-logging-plugin") <----------- on the directory and NOT the rootProject.name
    }
```

So now instead of what we used to have to do, we can now do:

```


    // shared code inside of each of the following:
    //    - projectRoot/someApplicationOne/build.gradle.kts
    //    - projectRoot/someApplicationTwo/build.gradle.kts
    //    - projectRoot/someLibraryOne/build.gradle.kts
    
    plugins {
        id("awesomeLargeProject.plugin.module.basic-java-project-plugin")
    }
    
    
    // All of this code is no longer needed, as is instead being coming from our basic-java-project-plugin
        
    // tasks.test {
    //     useJUnitPlatform()
    //     testLogging {
    //         events = setOf(
    //             TestLogEvent.FAILED,
    //             TestLogEvent.PASSED,
    //             TestLogEvent.SKIPPED,
    //         )
    //     }
    // }
    // 
    // java {
    //     toolchain {
    //         languageVersion.set(JavaLanguageVersion.of(17))
    //     }
    // }

```