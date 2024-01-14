# Monorepo

## Purpose

- A showcase of the work I do in my spare time for any future employers

- A place which can house multi-language setups. The right tool for the job, rather than attempting
to use a sledgehammer when a screwdriver would have been better

- Each language or 'toolset' will then be have a structure
which allows high throughput, but maximum accountability for the terrible code I wrote

- This means I'm forced to learn from mistakes over long periods of time, as there's nothing
more valuable than working amongst code you wrote, and having to deal with the trade-offs or short
term decisions you may or may not have kept putting off


## Current work in progress - IntegraBoost - Enhance your ability to write integration or e2e tests easily

### Inspiration/purpose

1. Feel as though there's just so much wasted time with config and build systems in JVM land
2. Astonishing that out of the box there is no easy to way have high performance tests setup
3. All of the performance options are normally OFF by default, and because the build systems feel
the need to support legacy systems, performance is on the back burner
4. This takes the approach that testing speeds are way too slow the majority of the time for what
they could be, and aims to investigate pragmatic ways of solving something people are 'just used to'
5. The current end goal feels like a full test product, which allows you to:
   1. Detect regressions (in terms of both speed for individual tasks and overall time). Meaning that you may
    never have realised you introduced regressions, or a regression alongside an improvement that balanced each other out
   2. It focuses on how we might reduce what is universally considered 'slow' tests (integration/e2e tests)
   3. Parallelism and concurrently are on by default
   4. New java version comes out? Cool we upgrading due to speed improvements
6. Make writing code fun again, by not having to have wait for so much compilation!

### Current short-term roadmap

1. Create new module representing the renaming of package structure and copy across contents of script from README 

2. Fix script for renaming package structure to include:
   1. Renaming files too
   2. Renaming any strings referenced in each of the files
   3. This should enable us move to next item in roadmap, which is copying the tasks from here: https://github.com/jjohannes/gradle-project-setup-howto/tree/main/gradle/plugins/dependency-analysis-plugins

3. Copy task that checks the dependencies for a module, and ensures they meet the following requirements:
   1. Alphabetical order
   2. Don't have version declared (should be pulling these in through platforms)
   3. Checks that versions declared in platform are actually used and are consistent with consistent resolution result
   4. Scoping is correct and outputs useful errors/suggestions if
      not
Note: Should be able to pretty much copy/paste after running our script for renaming package structures with a few slight modifications

4. Gradle config regressions can be fixed by using plugin testing
    1. We can then verify if we have regressed anything with our 'dependency adherence plugins', 
    by systematically testing prior expectations 
    2. This offers the fastest feedback loop
    3. Every other task would depend on this one
    4. This ensures that we validate our gradle scripts functionality before even running anything, and fail fast
    5. It also allows us to assert that from X commit we have coverage against Y,Z regressions which allow much faster upgrades

5. Now that we have all of gradle plugin functionality tested, we can copy across previous modules one-by-one 
to new structure, see: https://github.com/NoPhaseNoKill/monorepo/tree/285b90b6334971b2978ed0954c0220f0914ca917/modules

6. Add better output of logging for plugin/dependency management print statements in [kotlin-project-root-repositories.settings.gradle.kts](toolset%2Fjvm%2Fbuild-logic%2Fsettings%2Fkotlin-project-root-settings%2Fsrc%2Fmain%2Fkotlin%2Fkotlin-project-root-repositories.settings.gradle.kts)
7. Add folder in build-logic named tasks, and have sub-folders java/kotlin/root tasks etc 

### Create script to make a new module (under modules/applications or modules/libraries)
1. Create basic structure of project (ie something like: src/main/kotlin, src/main/resources, src/test/kotlin, src/test/resources)
2. Create gradle build files (this would mean we would need some way of templating this?)
    1. Could we possibly create a DSL syntax for this instead? Seems way better as alternative
3. Does this mean that we also need a 'modules/scripts' folder? Seems like it could be appropriate for ad-hoc things
   where you might want to build/compile something for re-use at the top level?

### Investigate creating profiler for build times
1. Start dirty, work way up to better. Run ./gradlew someGradleTaskHere 10 times for instance
2. Needs to be historically storable.
    1. git sha
    2. time (whatever metric that is)
    3. which specific command was run (buildAll versus compileAll versus X)
3. Needs to run each time gradle task is done + when committing.
4. Maybe docker image to isolate runs?
5. Needs to be run in parallel, so that you could run 20 isolated tasks, and if the longest task take took 7 seconds,
   7 seconds would be total amount to run this 'full process'

## Park bench ideas

1. Fix deprecation warnings for creation of task dependency trees
2. Way of figuring out whether you have inadvertently regressed the top level settings.gradle.kts/build files (ie broken buildAll etc)
3. Compilation improvements: https://kotlinlang.org/docs/gradle-compilation-and-caches.html
    1. Build reports: https://kotlinlang.org/docs/gradle-compilation-and-caches.html#build-reports
    2. Incremental compilation (precise backup): https://kotlinlang.org/docs/gradle-compilation-and-caches.html#incremental-compilation
    3. The Kotlin daemon and how to use it with Gradle: https://kotlinlang.org/docs/gradle-compilation-and-caches.html#the-kotlin-daemon-and-how-to-use-it-with-gradle
4. Does grouping logic in build-logic ACTUALLY improve performance? Consider the following example:
    1. You have some plugin which gets updated
    2. Does this plugin then invalidate caches concurrently of each of the projects using it?
    3. If no, how could we then have some shared logic still, but invalidate/revalidate the affected projects concurrently?
    4. Could we make our own using coroutines instead? Or with java 21, using virtual threads?
5. Make a proper DSL similar to the original modules applications/libraries one, but for plugins and platforms
    1. This will need to be an external plugin (separately managed/directory) so that the settings file can use a basic
       setup similar to what we're doing for root
    2. This gives you granularity between responsibilities for plugins, but also allows you dynamically configure them
       and not have to manually include them each time
6. Why does it feel completely wasteful having to declare constraints for dependencies in a build file, only to have
   to declare the dependencies again AND THEN declare dependencies on the plugin level?
    1. Duplication seems outrageous
    2. Is the folder structure actually wrong here? Maybe plugins needs to be similar to platforms (standalone)
       which sub-projects that define each of their own?
    3. Is it better to actually declare the specific versions and constraints in one file and throw errors
       when a user/person tries to manually add a dependency which does not declare both at the plugin level?
7. Templates feel like a much better name rather than plugins
8. Feels like you should absolutely have some logger/gradle property which effectively alerts the user if it's
a 'near' miss when settings up repositories. Feels like an absolute no-brainer, and I have wasted a good month
coming to the conclusion that this was what was causing my issues this whole time. Absolutely dreadful API, 0 feedback
and about a zillion chances to shoot yourself in the foot here.
   1. It's almost mandatory IMO that there should be some form of debug mode which easily allows turning on/off the deps,
   what they're being resolved to when trying to include them, the versions, group, name etc
9. Create custom task which automatically sets application main class rather than manual configuration
   1. Should throw error if you don't configure it
   2. Should iterate over any files with the name 'App', and find the one with the code: 'fun main(' , which indicates
   that this is the main method
10. Create library which is a wrapper around gradle to manage files/file trees easily
    1. Gradle's APIs feel extremely clunky and unintuitive
    2. This may form the start/creation of the DSL (see point 5)
11. Performance profile file encodings (UTF-8 versus UTF-16 versus X)
12. IntelliJ performance for creating indexes is mind-bogglingly slow
    1. Can we just autogenerate this? 
       1. If yes, instead of Java/Kotlin, this most likely should be built in C
       2. Before starting this, ensure you benchmark/profile properly, and validate this ASSUMPTION (which is what it
       is atm, it's just an assumption and NOT fact)
    2. From a given project, we should actually have well-defined inputs/outputs which enable us to thoroughly test this
    3. Downside of this is that we're tied to a version of intellij possibly - whereby bugs/nwe features may affect this
    4. Upside if that even if we have this downside, slowly we build an interface for the functionality expected by
    IntelliJ and can actually post REAL fixes based on specific versions
     
## Gradle learnings
1. Due to multiple build scripts running concurrently for composite builds, rather than a single entrypoint, misconfigurations of 
setting up your repositories will fail silently and are extremely hard to diagnose. This means when running tasks again,
unless you have a very specific idea of which tasks you're expecting to run (aka the number of them in larger projects),
you're most likely going to trip yourself up at some stage. 


## Useful commands

### Checking project dependency health

```
./gradlew build
```

### Running the gradle profiler

```
EPOCH_TIME=$(date +%s) &&
gradle-profiler --benchmark --iterations 100 --scenario-file ./gradle-profiler/scenarios/performance.scenarios --output-dir "./gradle-profiler/results/$EPOCH_TIME" --gradle-user-home './gradle-profiler/gradle-user-home' performance_both &&
unset EPOCH_TIME
```

### Walking file tree

```
   @OptIn(ExperimentalPathApi::class)
   rootDir.toPath().visitFileTree {
          onPreVisitDirectory { directory, _ ->
              if (directory.name == "build") {
                  directory.toFile().deleteRecursively()
                  FileVisitResult.SKIP_SUBTREE
              } else {
                  FileVisitResult.CONTINUE
              }
          }
     
          onVisitFile { file, _ ->
              if (file.extension == "class") {
                  file.deleteExisting()
              }
              FileVisitResult.CONTINUE
          }
      }
```

### Rename package structure

```
package com.nophasenokill.utils

import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.pathString

class FileUtilsTest {

    @Test
    fun `should rename directories based on group and platform`() {

        // Example usage
        val startingDir = "/home/tomga/projects/gradle-project-setup-howto"
        val from = "org/example"
        val to = "com/nophasenokill"

        renameDirectoriesRecursively(startingDir, from, to)
    }

    fun renameDirectoriesRecursively(startingDir: String, from: String, to: String) {
        val startingPath = Paths.get(startingDir)
        val fromPath = Paths.get(from)
        val toPath = Paths.get(to)

        Files.walkFileTree(startingPath, object : SimpleFileVisitor<Path>() {
            @Throws(IOException::class)
            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                if (dir.endsWith(fromPath)) {
                    val newDir = startingPath.resolve(dir.toString().replace(fromPath.toString(), toPath.toString()))
                    if(!File(newDir.pathString).exists()) {
                        File(newDir.pathString).mkdirs()
                    }
                    Files.move(dir, newDir, StandardCopyOption.REPLACE_EXISTING)

                    // Remove empty parent directories
                    var parent = dir.parent
                    while (parent != null && !parent.equals(startingPath)) {
                        if (parent.toFile().list().isEmpty()) {
                            Files.delete(parent)
                        }
                        parent = parent.parent
                    }

                    return FileVisitResult.SKIP_SUBTREE
                }
                return FileVisitResult.CONTINUE
            }
        })
    }
}
```

## Random unrelated scripts


### Convert youtube video between two timestamps to mp3

Example: This downloads the song 'glitter' from a tiny desk clip of Tyler the Creator

1. Go to: https://github.com/yt-dlp/yt-dlp/releases/latest
2. Download linux version: https://github.com/yt-dlp/yt-dlp/releases/download/2023.12.30/yt-dlp_linux
3. Assuming downloaded file is in ~/Downloads, run the following. Note: 'N1w-hDiJ4dM' for the output file name can be replaced with anything if you want
   ```
   ./yt-dlp_linux "https://www.youtube.com/watch?v=N1w-hDiJ4dM" -o N1w-hDiJ4dM.webm
   ```
4. Run below to convert to mp3:
   1. '797' is the start time in seconds
   2. '340' is the duration you want to clip it for in seconds
   3. 'glitter.mp3' is the output
   ```
   ffmpeg -ss 797 -t 340 -i N1w-hDiJ4dM.webm -vn -ab 128k glitter.mp3
   ```
5. Create and copy song to desktop directory named 'spotify-songs'
   ```
   mkdir spotify-songs && cp glitter.mp3 ~/Desktop/spotify-songs
   ```
6. Run below to add song tag metadata (in this case the title and artist):
   ```
   id3v2 -t "Glitter" -a "Tyler the Creator" ~/Desktop/spotify-songs/glitter.mp3
   ```

### Influence repositories I found along the way
https://github.com/blundell/monorepo
https://github.com/CXwudi/modern-gradle-template