<!-- TOC -->
  * [Current short-term/medium roadmap (Updated 10/09/2024)](#current-short-termmedium-roadmap-updated-10092024)
    * [10/09/2024 - Understanding the true elephant in the room](#10092024---understanding-the-true-elephant-in-the-room)
    * [16/03/2024 - The pain points and ways forward](#16032024---the-pain-points-and-ways-forward)
    * [14/02/2024 - Low parallelism, mainly just understanding/configuring gradle correctly](#14022024---low-parallelism-mainly-just-understandingconfiguring-gradle-correctly)
    * [12/02/2024 - Initial Summary](#12022024---initial-summary)
  * [Gradle learnings](#gradle-learnings)
  * [Useful commands](#useful-commands)
    * [Remove any references (.build, .gradle, ~/.gradle, .idea) to gradle caches](#remove-any-references-build-gradle-gradle-idea-to-gradle-caches)
    * [Running the gradle profiler](#running-the-gradle-profiler)
  * [Park bench ideas](#park-bench-ideas)
<!-- TOC -->

## Current short-term/medium roadmap (Updated 10/09/2024)

### 10/09/2024 - Understanding the true elephant in the room

It's been nearly 6 months since I last wrote an update. I've still been working
on this project, and have done just over 200 commits in that time.

I'll try and breakdown the majority of my findings into sections, that I feel are important.

The short summary:

1. Key build information is unintentionally obfuscated/difficult to track down, making it hard to understand
what is really happening.

2. Finding compatible gradle versions that work with everything I want has been near impossible

3. Gradle's core API is horrendous 

Because of these three things, I have been working still on trying to find the right layout, have been building
a UI with jetpack compose so that you can have a tool which helps surface the relevant info you need, while
continuing to use gradle itself to feel each of those pain points deeply so I can really wrap my head around
what's wrong with it.

The longer summary:

TODO

When I have a clearer understanding of suspected cache invalidation,
alongside buildSrc with includedBuilds fill this section out

### 16/03/2024 - The pain points and ways forward

One of my continual gripes with gradle (apart from what I would consider a MAJOR bug that I
found for 'core' functionality given their extension documentation of configuration avoidance -
https://github.com/gradle/gradle/issues/28331), was that it still feels hard to do the right thing.

After both gaining an immense amount knowledge in this area after the last few months, I continually
find myself tripping up on subtle nuances, bugs, and going 30 commits deep on a local branch to troubleshoot
issues. This isn't/wasn't sustainable.

So what do I actually need? How can I look to tackle these problems? What does this tell me in retrospect?

A major realisations (at a core emotional level lul):

* Not even the maintainers of a project pushing for configuration avoidance API's can properly
  test all different permutations of gradle. While the above bug should DEFINITELY have been caught
  (it's a default gradle init project) - there's one SIGNIFICANT downside to gradle: flexibility.
* I needed a very clear idea of what I was wanting to use gradle for and what a successful outcome was
* The current setup still did not allow me, at an automated level, to protect myself from regressions
  or even being aware what I was doing was a problem in the first place.

In comes the hidden gem that I discovered: org.gradle.internal.tasks.stats

This was useful for two primary things:

1. I could see whether I was adhering to gradle's configuration avoidance API's:

```
./gradlew testAll -Dorg.gradle.internal.tasks.stats

271 actionable tasks: 34 executed, 237 up-to-date
Task counts: Old API 0, New API 322, total 322
Task counts: created 134, avoided 188, %-lazy 59

Task types that were registered with the new API but were created anyways
class org.gradle.api.DefaultTask 10
class org.gradle.api.tasks.compile.JavaCompile 6
class com.autonomousapps.tasks.ExplodeJarTask 6
class com.autonomousapps.tasks.FindInlineMembersTask 6
class com.autonomousapps.tasks.SynthesizeDependenciesTask 6
class com.autonomousapps.tasks.ClassListExploderTask 6
class com.autonomousapps.tasks.FindServiceLoadersTask 6
class com.autonomousapps.tasks.ArtifactsReportTask 6
class com.autonomousapps.tasks.SynthesizeProjectViewTask 6
class com.autonomousapps.tasks.GraphViewTask 6
class org.gradle.language.jvm.tasks.ProcessResources 6
class com.autonomousapps.tasks.ComputeUsagesTask 6
class com.autonomousapps.tasks.FindDeclaredProcsTask 6
class org.jetbrains.kotlin.gradle.tasks.KotlinCompile 6
class com.autonomousapps.tasks.CodeSourceExploderTask 6
class com.autonomousapps.tasks.ProjectHealthTask 3
class com.autonomousapps.tasks.GenerateProjectHealthReportTask 3
class org.gradle.api.tasks.Delete 3
class org.gradle.api.tasks.bundling.Jar 3
class com.autonomousapps.tasks.ComputeAdviceTask 3
class com.autonomousapps.tasks.FilterAdviceTask 3
class com.autonomousapps.tasks.FindDeclarationsTask 3
class org.jetbrains.kotlin.gradle.plugin.diagnostics.CheckKotlinGradlePluginConfigurationErrors 3
class org.gradle.api.tasks.testing.Test 3
class com.autonomousapps.tasks.AbiAnalysisTask 2
class com.nophasenokill.ClasspathCollisionDetectorTask 2
class com.nophasenokill.DependencyFormatCheck 2
class com.nophasenokill.CreateMD5 2
class com.autonomousapps.tasks.DetectRedundantJvmPluginTask 2
class com.nophasenokill.CreateMD5 1
class com.nophasenokill.ClasspathCollisionDetectorTask 1
class com.nophasenokill.DependencyFormatCheck 1
Task counts: Old API 0, New API 22, total 22
Task counts: created 0, avoided 22, %-lazy 100
Task counts: Old API 0, New API 658, total 658
Task counts: created 201, avoided 457, %-lazy 70

Task types that were registered with the new API but were created anyways
class org.gradle.api.tasks.compile.JavaCompile 31
class org.gradle.language.jvm.tasks.ProcessResources 15
class org.gradle.api.DefaultTask 15
class org.gradle.plugin.devel.tasks.GeneratePluginDescriptors 15
class org.gradle.api.tasks.Delete 15
class org.gradle.api.tasks.bundling.Jar 15
class org.jetbrains.kotlin.gradle.tasks.KotlinCompile 15
class org.jetbrains.kotlin.gradle.plugin.diagnostics.CheckKotlinGradlePluginConfigurationErrors 15
class org.gradle.kotlin.dsl.provider.plugins.precompiled.tasks.GenerateScriptPluginAdapters 13
class org.gradle.kotlin.dsl.provider.plugins.precompiled.tasks.CompilePrecompiledScriptPluginPlugins 13
class org.gradle.kotlin.dsl.provider.plugins.precompiled.tasks.GeneratePrecompiledScriptPluginAccessors 13
class org.gradle.kotlin.dsl.provider.plugins.precompiled.tasks.GenerateExternalPluginSpecBuilders 13
class org.gradle.kotlin.dsl.provider.plugins.precompiled.tasks.ExtractPrecompiledScriptPluginPlugins 13
Task counts: Old API 0, New API 60, total 60
Task counts: created 14, avoided 46, %-lazy 77

Task types that were registered with the new API but were created anyways
class org.gradle.api.tasks.compile.JavaCompile 2
class org.gradle.kotlin.dsl.provider.plugins.precompiled.tasks.GenerateScriptPluginAdapters 1
class org.gradle.kotlin.dsl.provider.plugins.precompiled.tasks.CompilePrecompiledScriptPluginPlugins 1
class org.gradle.kotlin.dsl.provider.plugins.precompiled.tasks.GeneratePrecompiledScriptPluginAccessors 1
class org.gradle.language.jvm.tasks.ProcessResources 1
class org.gradle.api.DefaultTask 1
class org.gradle.plugin.devel.tasks.GeneratePluginDescriptors 1
class org.gradle.api.tasks.Delete 1
class org.gradle.api.tasks.bundling.Jar 1
class org.jetbrains.kotlin.gradle.tasks.KotlinCompile 1
class org.gradle.kotlin.dsl.provider.plugins.precompiled.tasks.GenerateExternalPluginSpecBuilders 1
class org.jetbrains.kotlin.gradle.plugin.diagnostics.CheckKotlinGradlePluginConfigurationErrors 1
class org.gradle.kotlin.dsl.provider.plugins.precompiled.tasks.ExtractPrecompiledScriptPluginPlugins 1
Task counts: Old API 0, New API 30, total 30
Task counts: created 7, avoided 23, %-lazy 77

Task types that were registered with the new API but were created anyways
class org.gradle.api.DefaultTask 7
```

2. I could also output stack traces to a file, to identify EXACTLY what was causing this:

```
./gradlew runAll -Dorg.gradle.internal.tasks.stats="output.txt"

java.lang.Throwable
	at org.gradle.api.internal.tasks.TaskStatistics.lazyTaskRealized(TaskStatistics.java:101)
	at org.gradle.api.internal.tasks.DefaultTaskContainer$TaskCreatingProvider.onLazyDomainObjectRealized(DefaultTaskContainer.java:704)
	at org.gradle.api.internal.DefaultNamedDomainObjectCollection$AbstractDomainObjectCreatingProvider.tryCreate(DefaultNamedDomainObjectCollection.java:1007)
	at org.gradle.api.internal.tasks.DefaultTaskContainer$TaskCreatingProvider.access$1401(DefaultTaskContainer.java:656)
	at org.gradle.api.internal.tasks.DefaultTaskContainer$TaskCreatingProvider$1.run(DefaultTaskContainer.java:682)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$1.execute(DefaultBuildOperationRunner.java:29)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$1.execute(DefaultBuildOperationRunner.java:26)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:66)
	at org.gradle.internal.operations.DefaultBuildOperationRunner$2.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:157)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.execute(DefaultBuildOperationRunner.java:59)
	at org.gradle.internal.operations.DefaultBuildOperationRunner.run(DefaultBuildOperationRunner.java:47)
	at org.gradle.internal.operations.DefaultBuildOperationExecutor.run(DefaultBuildOperationExecutor.java:68)
	at org.gradle.api.internal.tasks.DefaultTaskContainer$TaskCreatingProvider.tryCreate(DefaultTaskContainer.java:678)
	at org.gradle.api.internal.DefaultNamedDomainObjectCollection$AbstractDomainObjectCreatingProvider.calculateOwnValue(DefaultNamedDomainObjectCollection.java:990)
	at org.gradle.api.internal.provider.AbstractMinimalProvider.getOrNull(AbstractMinimalProvider.java:105)
	at org.gradle.api.internal.DefaultNamedDomainObjectCollection.findByName(DefaultNamedDomainObjectCollection.java:319)
	at org.gradle.api.internal.tasks.DefaultTaskContainer.findByName(DefaultTaskContainer.java:560)
	at org.gradle.api.internal.tasks.DefaultTaskContainer.findByName(DefaultTaskContainer.java:77)
	.....
```
This was a huge win. After many lost hours, continual banging of my head against a wall, I decided to sit down
and properly organise a way forward. And then it dawned on me....

1. I need to create my own plugin, which would serve as a way of version controlling
   and regression testing my own code (talked about this months ago - but now we had a reason
   to do it, and not just a feeling)
2. The minimal viable product aka, absolute alpha version of this now had 'hard' requirements of common things I've noticed
3. These requirements will be relatively straight forward to setup, and I will be able to use
   the learnings from the current setup to influence the direction of each of the 'features' (explained shortly)
   of said plugin I will add

The current known requirements are as follows. These are/were born from how I'd EXPECT to be able to use
gradle in an ideal world.

1. Everything needs to be versioned. It needs to go down all the way to the ABI layer. But in broader terms, after
   having learnt so much about 'capabilities', it means that I need every single capability of my plugin, and any internal
   modifications to those capabilities to be versioned
2. The versioning of these should also be tied to a 'current understandings' version. This will mean for instance,
   that when new knowledge is gained, or imposed by recommended gradle practises, we have a way of identifying
    1. Whether something is affected by this in a positive/negative way;
    2. Whether we stick to the old way for perf reasons
    3. What are the benchmarks for the different ways of doing this (code permutations) - so that we can ultimately
       just go with the fastest one once bench marking is done
3. The use of the plugin should be simple.
```
    
    # for an application
    
        plugins {
          id("com.nophasenokill.sensible-defaults.application") version X
        }
        
        application {
            mainClass = "com.nophasenokill.app.AppKt" // example here would be assuming structure of: rootDir/modules/applications/app/src/main/kotlin/com/nophasenokill/app/App.kt
        }
    
    # for a library
        
        plugins {
          id("com.nophasenokill.sensible-defaults.library") version X
        }

    
    # which under the hood will do things like

        plugins {
            id("com.nophasenokill.sensible-defaults.application")
            # or for a library
            # id("com.nophasenokill.sensible-defaults.library")
        }
        
        nophasenokill {
        
            # javaVersion = 21 // You will not be able to opt of this. If you cannot run on latest stablejava 21 - you should focus on upgrading that first, as that will likely be the biggest perf gain you'll get
            
            # languages = listOf(Language.KOTLIN) // default, but may be required to include/exclude KOTLIN/JAVA/MAYBE GROOVY!?
            
            # baseSourceCodeDir = "./modules" // default, but may be required to be changed
            
            # module/projectCategories = listOf("applications", "libraries") // default, but people may want control of this. this will set up modules/applications and modules/libraries build-scripts automatically
            
            # buildLogicDir/Name = "./build-logic" //default, but people may want specialised control over this if they ever need to add additional custom build logic on top of mine
            
            # featureConfig {
            #   Feature.OptimizeOsGradleCaches {
            #       allocatedRam: 4GB // default would be 1/4 or 1/2 of your RAM as an example - undecided
            #   }
            #
            #   sets up the rest of the features the same way as above
            #
            # } // does not allow you to turn them on/off. you are using this plugin for speed, that is the primary purpose
            
            # commitBenchmarkScript {
            #   location = "./commit-benchmark-script.sh" // default, people may want this configurable
            #   triggeredOn = "pre-commit" // default, you may want this on post-commit though
            # }
        }
```
4. This means that the minimal viable product will be a plugin with no features, it literally does nothing.
5. It means in future we can add the functionality currently written as plugins, as features:
    1. Java/kotlin config
    2. Some bench marking historically on pre-commit
    3. application/library default dependencies
    4. application/library default testing (junit)
    5. etc
6. We can now focus on the 'functionality' of our plugins one by one, write proper testing around them, and ensure
   regressions or unwanted side effects are highlighted/brought to our attention immediately

### 14/02/2024 - Low parallelism, mainly just understanding/configuring gradle correctly
The benchmark testing I have done (based on the build scans at the time of writing) were:

1. Build scan when going from a cold cache/with no dependencies, aka './gradlew :runAllMainBuildTasks --rerun-tasks --refresh-dependencies --scan': https://scans.gradle.com/s/45nfivplxf6m2
2. I then ran './gradlew :runAllMainBuildTasks --scan' which gave: https://scans.gradle.com/s/l6nrbxtrgrjlo
3. Followed up by changing a single file in the utilities test folder: https://scans.gradle.com/s/lh7wfafqrwuq4/performance/build

The promising thing about this was:
1. Dependency downloads took roughly ~3/4 of the build scan wall clock time for the initial cold cache/no cache (indicating the idea around dependency splitting for parallelism has merit)
2. The absolute shortest time from a cache this build could have taken was just over 3 seconds,  (due to me putting in an absolute minimum test time of 3 seconds).
   See: :modules:libraries:utilities:sourceFileHashingPluginTask here: https://scans.gradle.com/s/l6nrbxtrgrjlo/timeline?sort=longest
   2.172s	3.005s	com.nophasenokill.CreateMD5
   If everything else worked completely in parallel, this gives us an indication that there are approx 2.188s being wasted (something to work off as a baseline),
   as the 'extra' time the whole build took was 5.188s (total) - 3.005s = 2.185s. This is already a HUGE improvement over maven
3. Changing a single file, of which all applications/libraries were dependant on, meant that the avoidance savings (ie re-usable cache)
   from step 2 only decreased from 85.05% -> 83.73%

Stats were:

Step 2:
- All tasks: 290
- Tasks avoided: 190
- Avoidance savings: 58.505s (85.05%)
- Total wall clock time: 5.188s
  -290 tasks, 0 transforms executed in 16 projects in 5.188s , with 190 avoided tasks saving 58.505s

Step 3:
- All tasks: 290
- Tasks avoided: 186
- Avoidance savings: 57.578s (83.73%)
- Total wall clock time: 5.587s
- 290 tasks, 0 transforms executed in 16 projects in 5.587s with 186 avoided tasks saving 57.578s

Keeping in mind these are benchmarks for what I'll say are still 'relatively unoptimized' (aka very little work has been
done to keep them in parallel) - this an absolute amazing result so far.

### 12/02/2024 - Initial Summary

Turns out our idea has merit. Large merit. Big enough merit that I ran a single benchmark test (normally would do approx 100,
in an actual container rather than on a host OS), and instantly knew this was a go-ahead. Please see above if you interested.

The following are in no particular order:

1. Add testing to all plugins/properly ensure that we don't break backwards compatibility we care about

2. Create a systemic way to actually measure these builds properly
    1. This probably includes creating our own gradle profiler (gradle's one does not expose meaningful/useful enough info)
    2. We probably need a docker container with certain specs (rather than our own OS), to get more consistent results
    3. This will give us our 'actual' baseline, rather than just generalised baseline/benchmark we have now

3. Enable as much parallelism using gradle's WorkQueue (see source-file-hashing-plugin) as possible. This may include things like:
    1. Dependency resolution. All found here: https://docs.gradle.org/current/userguide/performance.html#dependency_resolution
    2. Configuration avoidance
        1. To enable this, we firstly need to migrate the logger to the new useService
        2. We also probably need to look at getting rid of the dependency analysis plugin - as this uses lots of deprecated features
        3. Maybe: Split each singular dependency into its own project, so we can get maximum concurrency. This means each
           node in the tree gets its own project, but would be responsible for a singular fetch -> enabling extremely high speeds
           when fetching packages and configuring projects
    3. Enable remote build cache
        1. https://docs.gradle.org/current/userguide/part6_gradle_caching.html#step_4_understanding_remote_caching
        2. https://docs.gradle.com/build-cache-node/#installation
        3. https://docs.gradle.com/build-cache-node/#version_history
        4. https://hub.docker.com/r/gradle/build-cache-node

4. Organisation of plugins still feels a bit weird. There's a concept of 'meta-plugins' already occurring which:
    1. Means we firstly have a dependency on the kotlin version being used for gradle
    2. We then have a dependency on the dependency-analysis=plugin (to check our platform dependencies)
    3. Majority of other stuff can then be built/done with that
    4. How could we optimize this is the real question here?

5. Add better output which may analyse inefficiency of dependency resolution. This could be things like:
    1. Suggested ordering
    2. Something you may be doing inadvertently that is having a major impact
    3. Output of the largest wall clock time syncs so you can investigate

6. Continue from GenerateMD5.kt: https://docs.gradle.org/current/userguide/worker_api.html#changing_the_isolation_mode


## Gradle learnings
1. Due to multiple build scripts running concurrently for composite builds, rather than a single entrypoint, misconfigurations of
   setting up your repositories will fail silently and are extremely hard to diagnose. This means when running tasks again,
   unless you have a very specific idea of which tasks you're expecting to run (aka the number of them in larger projects),
   you're most likely going to trip yourself up at some stage. To fix this type of issue, I have created an attempt to
   protect/make it as hard as possible for people to trip themselves up.
   See: commit at time of: 6a5078fa085c5782d6870a1c9e8ef8fc2ecfe877
   [settings.gradle.kts](toolset%2Fjvm%2Fbuild-logic%2Fsettings%2Fsettings.gradle.kts) and
   [kotlin-project-root-repositories.settings.gradle.kts](toolset%2Fjvm%2Fbuild-logic%2Fsettings%2Fkotlin-project-root-settings%2Fsrc%2Fmain%2Fkotlin%2Fkotlin-project-root-repositories.settings.gradle.kts)

2. When using include, Gradle only needs the leaves of the tree. This means that
   using 'include("services:hotels:api")', it will create three projects:
   'services' 'services:hotels' and 'services:hotels:api'
    1. A nuance of this is to do with dependency resolution when you mis-configure it.
       Because you are now including many more projects than you may want to (ie
       all intermediary directories), you quickly run into issues.

       Examples of such issues may be:
        - build speed slowly degrading before
        - all of a sudden, you realise just how slow it is.
        - Getting strange results when trying to import a project dependency that you SWORE  worked very recently (ie project(":list").
    2. This has tripped me up several times now, and ultimately the wrong to assist in trying to not regress everything
       is via a test suite. Gaining an understanding of the pain points is crucial, as it'll allow me configure everything
       in a way which prevents me from having the same issue I've already had

   More details can be found here: https://docs.gradle.org/current/userguide/fine_tuning_project_layout.html#sub:building_the_tree

## Useful commands

### Remove any references (.build, .gradle, ~/.gradle, .idea) to gradle caches

```
./remove-gradle-cache.sh
```

### Running the gradle profiler

```
EPOCH_TIME=$(date +%s) &&
gradle-profiler --benchmark --iterations 100 --scenario-file ./gradle-profiler/scenarios/performance.scenarios --output-dir "./gradle-profiler/results/$EPOCH_TIME" --gradle-user-home './gradle-profiler/gradle-user-home' performance_both &&
unset EPOCH_TIME
```

## Park bench ideas

1. Build performance tracking
    1. See: BuildPerformanceMetric from package org.jetbrains.kotlin.build.report.metrics and
       package org.jetbrains.kotlin.gradle.report TaskExecutionResult/TaskExecutionInfo
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
13. File reproducibility around concurrency. Consider carefully what value we choose in future
    1. See: https://docs.gradle.org/current/userguide/working_with_files.html#sec:reproducible_archives
    2. To me, it wasn't immediately obvious why, but the context is:

        - Archives are based on timestamps

        - Different computers (remote cache, versus local, versus dev to dev)
          would then technically have different inputs and outputs

        - This would render the cache a miss even on the same inputs/outputs with
          the only difference being WHEN it was run

        - This can be confusing where conceptually most people have thought
          this would be a build cache hit (and it now is).

    Note: While there is currently no need to have this on, there may come a time
    where we do want to render it a build cache miss. The first example that
    springs to mind is something like:
    - Scheduled task
    - Runs on CI every night
    - It's based on the latest git sha/commit in the repo
    - This may mean if someone doesn't commit to said repo, you still want the build cache
      to miss, because this represents two consecutive days (separate from each other)
    - However, I'd argue this then just modelled with the wrong inputs/outputs
14. Create script to make a new module (under modules/applications or modules/libraries)
    1. Create basic structure of project (ie something like: src/main/kotlin, src/main/resources, src/test/kotlin, src/test/resources)
    2. Create gradle build files (this would mean we would need some way of templating this?)
        1. Could we possibly create a DSL syntax for this instead? Seems way better as alternative
    3. Does this mean that we also need a 'modules/scripts' folder? Seems like it could be appropriate for ad-hoc things
       where you might want to build/compile something for re-use at the top level?

15. Investigate creating profiler for build times
    1. Start dirty, work way up to better. Run ./gradlew someGradleTaskHere 10 times for instance
    2. Needs to be historically storable.
        1. git sha
        2. time (whatever metric that is)
        3. which specific command was run (buildAll versus compileAll versus X)
    3. Needs to run each time gradle task is done + when committing.
    4. Maybe docker image to isolate runs?
    5. Needs to be run in parallel, so that you could run 20 isolated tasks, and if the longest task take took 7 seconds,
       7 seconds would be total amount to run this 'full process'
