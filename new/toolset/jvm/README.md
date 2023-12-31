# IntegraBoost - Enhance your ability to write integration or e2e tests easily

## Work in progress

### Migrate from old to new structure
   1. bring in dependency conflict/duplicate identifier package
      ```
      // taken from previous branch
      
      build.gradle.kts
      
      plugins {
      id("com.autonomousapps.dependency-analysis") version "1.26.0"
      /*
      Do not use `kotlin-dsl`. Under the covers it adds
      the equivalent of id("java-library") which introduces
      unnecessary dependencies. See: https://dev.to/autonomousapps/the-proper-care-and-feeding-of-your-gradle-build-d8g#redundant-plugins
      */
      //TODO Move to build-platforms/build-logic
      `kotlin-dsl-base`
      }
      
      logger.lifecycle("Kotlin build version is: $embeddedKotlinVersion")
      
      repositories {
      mavenCentral()
      }
      
      dependencyAnalysis {
      issues {
      all {
      onAny {
      severity("fail")
      }
      }
      }
      }
      ```
   2. create plugins for both kotlin application and kotlin library (maybe also combine things to commons etc)
   3. create some test plugin (maybe this is a platform?), which might copy across junit properties from the base plugin (ensuring parallel is set by default for example)

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

1. Way of figuring out whether you have inadvertently regressed the top level settings.gradle.kts/build files (ie broken buildAll etc)

## Useful commands

### Running the gradle profiler

```
EPOCH_TIME=$(date +%s) &&
gradle-profiler --benchmark --iterations 100 --scenario-file ./gradle-profiler/scenarios/performance.scenarios --output-dir "./gradle-profiler/results/$EPOCH_TIME" --gradle-user-home './gradle-profiler/gradle-user-home' performance_both &&
unset EPOCH_TIME
```
