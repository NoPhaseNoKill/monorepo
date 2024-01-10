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


### Inspiration/purpsoe

1. Feel as though there's just so much wasted time with config and build systems in JVM land
2. Astonishing that out of the box there is no easy to way have high performance tests setup
3. All of the performance options are normally OFF by default, and because the build systems feel
the need to support legacy systems, performance is on the back burner
4. This takes the approach that testing speeds are way too slow the majority of the time for what
they could be, and aims to investigate pragmatic ways of solving something people are 'just used to'
5. The current endgoal feels like a full test product, which allows you to:
   1. Detect regressions (in terms of both speed for individual tasks and overall time). Meaning that you may
    never have realised you introduced regressions, or a regression alongside an improvement that balanced each other out
   2. It focuses on how we might reduce what is universally considered 'slow' tests (integration/e2e tests)
   3. Parallelism and concurrently are on by default
   4. New java version comes out? Cool we upgrading due to speed improvements
6. Make writing code fun again, by not having to have wait for so much compilation!

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


## Useful commands

## Checking project dependency health

```
./gradlew build
```

### Running the gradle profiler

```
EPOCH_TIME=$(date +%s) &&
gradle-profiler --benchmark --iterations 100 --scenario-file ./gradle-profiler/scenarios/performance.scenarios --output-dir "./gradle-profiler/results/$EPOCH_TIME" --gradle-user-home './gradle-profiler/gradle-user-home' performance_both &&
unset EPOCH_TIME
```

## Walking file tree

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

### Influence repositories I found along the way
https://github.com/blundell/monorepo
https://github.com/CXwudi/modern-gradle-template