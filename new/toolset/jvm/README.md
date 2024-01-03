# IntegraBoost - Enhance your ability to write integration or e2e tests easily

## Work in progress

### Migrate from old to new structure
   1. Separate platforms to logical folders/better names
   2. Move java plugins to kotlin plugins
   3. Create some test plugin (maybe this is a platform?), which might copy across junit properties from the base plugin (ensuring parallel is set by default for example)
   4. Add check into tasks at root level to see whether dependency issues were introduced (update below useful command when implementing this)
   5. Ensure that cleanAll task removes top level build folder (related to projectHealth command)

### Create script to make a new module (under modules/applications or modules/libraries)
   1. Create basic structure of project (ie something like: src/main/kotlin, src/main/resources, src/test/kotlin, src/test/resources)
   2. Create gradle build files (this would mean we would need some way of templating this?)
      1. Could we possibly create a DSL syntax for this instead? Seems way better as alternative
   3. Does this mean that we also need a 'modules/scripts' folder? Seems like it could be appropriate for ad-hoc things
      where you might want to build/compile something for re-use at the top level?

### Investigate creating profiler for build times
   1. Remove gradle profiler
   2. Start dirty, work way up to better. Run ./gradlew someGradleTaskHere 10 times for instance
   3. Needs to be historically storable. 
      1. git sha
      2. time (whatever metric that is)
      3. which specific command was run (buildAll versus compileAll versus X)
   4. Needs to run each time gradle task is done + when committing.
   5. Maybe docker image to isolate runs?
   6. Needs to be run in parallel, so that you could run 20 isolated tasks, and if the longest task take took 7 seconds, 
      7 seconds would be total amount to run this 'full process'

## Park bench ideas

1. Way of figuring out whether you have inadvertently regressed the top level settings.gradle.kts/build files (ie broken buildAll etc)
2. Compilation improvements: https://kotlinlang.org/docs/gradle-compilation-and-caches.html
   1. Build reports: https://kotlinlang.org/docs/gradle-compilation-and-caches.html#build-reports
   2. Incremental compilation (precise backup): https://kotlinlang.org/docs/gradle-compilation-and-caches.html#incremental-compilation
   3. The Kotlin daemon and how to use it with Gradle: https://kotlinlang.org/docs/gradle-compilation-and-caches.html#the-kotlin-daemon-and-how-to-use-it-with-gradle
3. Does grouping logic in build-logic ACTUALLY improve performance? Consider the following example:
   1. You have some plugin which gets updated
   2. Does this plugin then invalidate caches concurrently of each of the projects using it?
   3. If no, how could we then have some shared logic still, but invalidate/revalidate the affected projects concurrently?
   4. Could we make our own using coroutines instead? Or with java 21, using virtual threads?

## Useful commands

## Checking project dependency health

```
./gradlew buildHealth
```

### Running the gradle profiler

```
EPOCH_TIME=$(date +%s) &&
gradle-profiler --benchmark --iterations 100 --scenario-file ./gradle-profiler/scenarios/performance.scenarios --output-dir "./gradle-profiler/results/$EPOCH_TIME" --gradle-user-home './gradle-profiler/gradle-user-home' performance_both &&
unset EPOCH_TIME
```
