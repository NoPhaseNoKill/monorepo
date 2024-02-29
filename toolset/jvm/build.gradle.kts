

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "8.6"
}

/*
    BELOW THIS IS WHAT GETS RUN FOR THE INTELLIJ RUN THROUGH

    Attribute(jvmArguments, Type(text))
    -XX:ErrorFile=/home/tomga/java_error_in_idea_%p.log
    -XX:HeapDumpPath=/home/tomga/java_error_in_idea_.hprof
    -Xms128m
    -Xmx2048m
    -XX:ReservedCodeCacheSize=512m
    -XX:+UseG1GC
    -XX:SoftRefLRUPolicyMSPerMB=50
    -XX:CICompilerCount=2
    -XX:+HeapDumpOnOutOfMemoryError
    -XX:-OmitStackTraceInFastThrow
    -XX:+IgnoreUnrecognizedVMOptions
    -XX:CompileCommand=exclude,com/intellij/openapi/vfs/impl/FilePartNodeRoot,trieDescend
    -ea
    -Dsun.io.useCanonCaches=false
    -Dsun.java2d.metal=true
    -Djbr.catch.SIGABRT=true
    -Djdk.http.auth.tunneling.disabledSchemes=""
    -Djdk.attach.allowAttachSelf=true
    -Djdk.module.illegalAccess.silent=true
    -Dkotlinx.coroutines.debug=off
    -Dsun.tools.attach.tmp.only=true
    -Dawt.lock.fair=true
    -Xmx4096m
    -Dide.managed.by.toolbox=/home/tomga/.local/share/JetBrains/Toolbox/bin/jetbrains-toolbox
    -Dtoolbox.notification.token=9ce55a41-4b7e-4c6f-b6e1-8911394e9095
    -Dtoolbox.notification.portFile=/home/tomga/.cache/JetBrains/Toolbox/ports/1781205792.port
    -Djb.vmOptionsFile=/home/tomga/.config/JetBrains/IntelliJIdea2023.3/idea64.vmoptions
    -Djava.system.class.loader=com.intellij.util.lang.PathClassLoader
    -Didea.vendor.name=JetBrains
    -Didea.paths.selector=IntelliJIdea2023.3
    -Djna.boot.library.path=/home/tomga/.local/share/JetBrains/Toolbox/apps/intellij-idea-ultimate/lib/jna/amd64
    -Dpty4j.preferred.native.folder=/home/tomga/.local/share/JetBrains/Toolbox/apps/intellij-idea-ultimate/lib/pty4j
    -Djna.nosys=true -Djna.noclasspath=true
    -Dintellij.platform.runtime.repository.path=/home/tomga/.local/share/JetBrains/Toolbox/apps/intellij-idea-ultimate/modules/module-descriptors.jar
    -Dsplash=true
    -Daether.connector.resumeDownloads=false
    --add-opens=java.base/java.io=ALL-UNNAMED
    --add-opens=java.base/java.lang=ALL-UNNAMED
    --add-opens=java.base/java.lang.ref=ALL-UNNAMED
    --add-opens=java.base/java.lang.reflect=ALL-UNNAMED
    --add-opens=java.base/java.net=ALL-UNNAMED
    --add-opens=java.base/java.nio=ALL-UNNAMED
    --add-opens=java.base/java.nio.charset=ALL-UNNAMED
    --add-opens=java.base/java.text=ALL-UNNAMED
    --add-opens=java.base/java.time=ALL-UNNAMED
    --add-opens=java.base/java.util=ALL-UNNAMED
    --add-opens=java.base/java.util.concurrent=ALL-UNNAMED
    --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED
    --add-opens=java.base/java.util.concurrent.locks=ALL-UNNAMED
    --add-opens=java.base/jdk.internal.vm=ALL-UNNAMED
    --add-opens=java.base/sun.nio.ch=ALL-UNNAMED
    --add-opens=java.base/sun.nio.fs=ALL-UNNAMED
    --add-opens=java.base/sun.security.ssl=ALL-UNNAMED
    --add-opens=java.base/sun.security.util=ALL-UNNAMED
    --add-opens=java.base/sun.net.dns=ALL-UNNAMED
    --add-opens=java.desktop/com.sun.java.swing.plaf.gtk=ALL-UNNAMED
    --add-opens=java.desktop/java.awt=ALL-UNNAMED
    --add-opens=java.desktop/java.awt.dnd.peer=ALL-UNNAMED
    --add-opens=java.desktop/java.awt.event=ALL-UNNAMED
    --add-opens=java.desktop/java.awt.image=ALL-UNNAMED
    --add-opens=java.desktop/java.awt.peer=ALL-UNNAMED
    --add-opens=java.desktop/java.awt.font=ALL-UNNAMED
    --add-opens=java.desktop/javax.swing=ALL-UNNAMED
    --add-opens=java.desktop/javax.swing.plaf.basic=ALL-UNNAMED
    --add-opens=java.desktop/javax.swing.text.html=ALL-UNNAMED
    --add-opens=java.desktop/sun.awt.X11=ALL-UNNAMED
    --add-opens=java.desktop/sun.awt.datatransfer=ALL-UNNAMED
    --add-opens=java.desktop/sun.awt.image=ALL-UNNAMED
    --add-opens=java.desktop/sun.awt=ALL-UNNAMED
    --add-opens=java.desktop/sun.font=ALL-UNNAMED
    --add-opens=java.desktop/sun.java2d=ALL-UNNAMED
    --add-opens=java.desktop/sun.swing=ALL-UNNAMED
    --add-opens=java.desktop/com.sun.java.swing=ALL-UNNAMED
    --add-opens=jdk.attach/sun.tools.attach=ALL-UNNAMED
    --add-opens=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED
    --add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED
    --add-opens=jdk.jdi/com.sun.tools.jdi=ALL-UNNAMED
 */

// this = {DaemonCommandExecuter@2834}
// connection = {DefaultDaemonConnection@2820} "DefaultDaemonConnection: socket connection from /127.0.0.1:36671 to /127.0.0.1:47708"
// command = {Build@2821} "Build{id=307b9205-f759-4474-b8f8-1b188a5456cc, currentDir=/home/tomga/projects/monorepo/toolset/jvm}"
// daemonContext = {DefaultDaemonContext@2822} "DefaultDaemonContext[uid=3a603cfb-6677-4a31-af63-456f4ae6cbdc,javaHome=/home/tomga/.sdkman/candidates/java/21.0.2-ms,daemonRegistryDir=/home/tomga/.gradle/daemon,pid=21914,idleTimeout=10800000,priority=NORMAL,applyInstrumentationAgent=true,daemonOpts=-XX:MaxMetaspaceSize=384m,-XX:+HeapDumpOnOutOfMemoryError,--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED,-agentlib:jdwp=transport=dt_socket,server=n,suspend=y,address=127.0.0.1:38639,--add-opens=java.base/java.util=ALL-UNNAMED,--add-opens=java.base/java.lang=ALL-UNNAMED,--add-opens=java.base/java.lang.invoke=ALL-UNNAMED,--add-opens=java.prefs/java.util.prefs=ALL-UNNAMED,--add-opens=java.base/java.nio.charset=ALL-UNNAMED,--add-opens=java.base/java.net=ALL-UNNAMED,--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED,-Xmx2g,-Dfile.encoding=UTF-8,-Duser.country=AU,-Duser.language=en,-Duser.variant]"
// daemonStateControl = {DaemonStateCoordinator@2823}
// configuration = {DefaultDaemonServerConfiguration@2819}
// actions = {RegularImmutableList@2824}  size = 14



// Starting Gradle Daemon...
// Connected to the target VM, address: '127.0.0.1:39833', transport: 'socket'
// Gradle Daemon started in 805 ms
//         Breakpoint reached at org.gradle.launcher.daemon.server.exec.DaemonCommandExecuter.executeCommand(DaemonCommandExecuter.java:48)
// Breakpoint reached
//         at org.gradle.launcher.daemon.server.exec.DaemonCommandExecuter.executeCommand(DaemonCommandExecuter.java:48)
// at org.gradle.launcher.daemon.server.DefaultIncomingConnectionHandler$ConnectionWorker.handleCommand(DefaultIncomingConnectionHandler.java:161)
// at org.gradle.launcher.daemon.server.DefaultIncomingConnectionHandler$ConnectionWorker.receiveAndHandleCommand(DefaultIncomingConnectionHandler.java:134)
// at org.gradle.launcher.daemon.server.DefaultIncomingConnectionHandler$ConnectionWorker.run(DefaultIncomingConnectionHandler.java:122)
// at org.gradle.internal.concurrent.ExecutorPolicy$CatchAndRecordFailures.onExecute(ExecutorPolicy.java:64)
// at org.gradle.internal.concurrent.AbstractManagedExecutor$1.run(AbstractManagedExecutor.java:47)
// at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
// at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
// at java.lang.Thread.runWith(Thread.java:1596)
// at java.lang.Thread.run(Thread.java:1583)
// Reusing configuration cache.
// > Task :modules:applications:app:processResources NO-SOURCE
// > Task :modules:libraries:list:processResources NO-SOURCE
// > Task :modules:libraries:utilities:processResources NO-SOURCE
// > Task :modules:libraries:list:checkKotlinGradlePluginConfigurationErrors
// > Task :modules:applications:app:checkKotlinGradlePluginConfigurationErrors
// > Task :modules:libraries:utilities:checkKotlinGradlePluginConfigurationErrors
// > Task :modules:applications:app:junitPropertiesCreationTask UP-TO-DATE
// > Task :modules:libraries:list:junitPropertiesCreationTask UP-TO-DATE
// > Task :modules:libraries:utilities:junitPropertiesCreationTask UP-TO-DATE
// > Task :modules:applications:app:processTestResources UP-TO-DATE
// > Task :modules:libraries:list:processTestResources UP-TO-DATE
// > Task :modules:libraries:utilities:processTestResources UP-TO-DATE
// > Task :modules:libraries:list:compileKotlin UP-TO-DATE
// > Task :modules:libraries:list:compileJava NO-SOURCE
// > Task :modules:libraries:list:classes UP-TO-DATE
// > Task :modules:libraries:list:jar UP-TO-DATE
// > Task :modules:libraries:utilities:compileKotlin UP-TO-DATE
// > Task :modules:libraries:list:compileTestKotlin UP-TO-DATE
// > Task :modules:libraries:utilities:compileJava NO-SOURCE
// > Task :modules:libraries:utilities:classes UP-TO-DATE
// > Task :modules:libraries:list:compileTestJava NO-SOURCE
// > Task :modules:libraries:list:testClasses UP-TO-DATE
// > Task :modules:libraries:utilities:jar UP-TO-DATE
// > Task :modules:applications:app:compileKotlin UP-TO-DATE
// > Task :modules:libraries:utilities:compileTestKotlin UP-TO-DATE
// > Task :modules:applications:app:compileJava NO-SOURCE
// > Task :modules:libraries:utilities:compileTestJava NO-SOURCE
// > Task :modules:applications:app:classes UP-TO-DATE
// > Task :modules:libraries:utilities:testClasses UP-TO-DATE
// > Task :modules:libraries:list:test UP-TO-DATE
// > Task :modules:libraries:utilities:test UP-TO-DATE
// > Task :modules:applications:app:compileTestKotlin UP-TO-DATE
// > Task :modules:applications:app:compileTestJava NO-SOURCE
// > Task :modules:applications:app:testClasses UP-TO-DATE
// > Task :modules:applications:app:test UP-TO-DATE
// BUILD SUCCESSFUL in 4s
//         20 actionable tasks: 3 executed, 17 up-to-date
// Configuration cache entry reused.
// 9:19:55 pm: Execution finished 'test'.
// Disconnected from the target VM, address: '127.0.0.1:39833', transport: 'socket'







/*
    TODO investigate below:

    1. is intellij basically just labelling this as buildSrc!?
        - http://localhost:63342/jvm/toolset/jvm/build/reports/configuration-cache/58voi8s0p8os496rfgf3zca1x/559aimx9vnuhdbif7ddvnv78v/configuration-cache-report.html?_ijt=lksc6c3ucub5jmuip1pb58u86m&_ij_reload=RELOAD_ON_SAVE
    2. See these too:
        ? kotlin.daemon.custom.run.files.path.for.tests
        ? kotlin.daemon.debug.log
        ? kotlin.daemon.enabled
        ? kotlin.daemon.jvm.options
        ? kotlin.daemon.log.path
        ? kotlin.daemon.options
        ? kotlin.daemon.perf
        ? kotlin.daemon.socket.backlog.size
        ? kotlin.daemon.socket.connect.attempts
        ? kotlin.daemon.socket.connect.interval
        ? kotlin.daemon.startup.timeout
        ? kotlin.daemon.verbose
        ? kotlin.environment.keepalive

    3. What are these files?
        ? CustomPropertiesFileValueSource, properties file /home/tomga/projects/monorepo/toolset/jvm/build-logic/plugins/local.properties
        ? CustomPropertiesFileValueSource, properties file /home/tomga/projects/monorepo/toolset/jvm/local.properties
    4. May need to execute task at runtime, something like:
            doFirst {
        logger.lifecycle("Starting tests")
        val out = ByteArrayOutputStream()
        val result: ExecResult = project.exec {
            this.args = listOf("sh", "-c", "echo \$XDG_DATA_HOME")
            this.standardOutput = out
        }

        logger.lifecycle(out.toString().trim())
    }
 */

// gradle.taskGraph.whenReady {
//     this.allTasks.forEach {
//
//         it.args.forEach {
//             println("Printing arg: ${it}")
//         }
//
//         exec {
//
//         }
//     }
// }
// tasks.register("checkFeatures") {
//     group = "verification"
//     description = "Run all feature tests"
//     // dependsOn(tasks.test)
//     dependsOn(gradle.includedBuild("modules").task(":applications:app:test"))
//     dependsOn(gradle.includedBuild("modules").task(":modules:libraries:list:test"))
//     dependsOn(gradle.includedBuild("modules").task(":modules:libraries:utilities:test"))
// }

// gradle.projectsEvaluated {
//     gradle.taskGraph.whenReady {
//         val tasks = this.allTasks
//         tasks.forEach { task ->
//             println("OHAI")
//             when (task) {
//                 is org.gradle.api.tasks.Exec -> println("Executing command: ${task.commandLine}")
//                 else -> println("Not exec. Is; ${task::class.java.name}")
//             }
//         }
//     }
// }

// plugins {
//     /*
//         Plugin that outputs performance metrics of the gradle command that was run to the $rootDir/build
//         directory. This can be useful in diagnosing bottlenecks, long-running tasks, or simply
//         to get an idea of the order in which tasks are run.
//
//         The intended long-term plan of this is to get a local equivalent of the build scan, which can then be used
//         to store information in a database for better observations of build improvements or degradation.
//      */
//     id("performance-metrics-plugin")
// }
//
// group = "com.nophasenokill.jvm"
//
// /*
//
//     This file contains primarily two tasks you should run:
//         - runAll or;
//         - cleanRunAll
//
//     Alternatively, run ./gradlew tasks for a full list of exposed tasks. Other
//     useful tasks are exposed, or available directly through the normal :path:to:task.
//
//     The only difference between runAll and cleanRunAll are that the second one
//     ensures it cleans everything first, and that the clean task for each of the projects runs before anything else does.
// */
//
// /*
//     Configure the ':tasks' task of the root project to only show
//     the main lifecycle tasks as entry points to the build
//  */
// val mainBuildGroup = "main build"
// tasks.named<TaskReportTask>("tasks") {
//     displayGroup = mainBuildGroup
// }
//
// val runAll = tasks.register("runAll") {
//     group = mainBuildGroup
//     description = "Runs all of the main build sub-tasks"
//
//     dependsOn(subTaskCheckDependenciesAll)
//     dependsOn(subTaskDetectClasspathCollisionsAll)
//     dependsOn(subTaskProjectHealthAll)
//     dependsOn(subTaskSourceFileHashingRunAll)
//     dependsOn(subTaskTestAll)
//     dependsOn(subTaskPrintRuntimeClasspathAll)
// }
//
//
// tasks.register("cleanRunAll") {
//     group = mainBuildGroup
//     description = "Cleans everything first, and then runs all of the main build sub-tasks"
//     /*
//         This ensures that the clean/build tasks are run initially, so that all build files are retained.
//         Without this, we were noticing that the runAllSourceFileHashingTasks would result in a build folder
//         that didn't contain any of the build files (as if the runAllSourceFileHashingTasks had re-created
//         the folder)
//      */
//     dependsOn(subTaskCleanAll)
//     finalizedBy(runAll)
// }
//
//
// val subTaskTestAll = tasks.register("subTaskTestAll") {
//     group = mainBuildGroup
//     description = "Runs all of the modules' tests"
//
//     dependsOn(gradle.includedBuild("modules").task(":applications:app:test"))
//     dependsOn(gradle.includedBuild("modules").task(":modules:libraries:list:test"))
//     dependsOn(gradle.includedBuild("modules").task(":modules:libraries:utilities:test"))
//
// }
//
// val subTaskCleanAll = tasks.register("subTaskCleanAll") {
//     group = mainBuildGroup
//     description = "Cleans each of the modules' sub projects"
//
//     dependsOn(gradle.includedBuild("modules").task(":applications:app:clean"))
//     dependsOn(gradle.includedBuild("modules").task(":modules:libraries:list:clean"))
//     dependsOn(gradle.includedBuild("modules").task(":modules:libraries:utilities:clean"))
// }
//
// val subTaskCheckDependenciesAll = tasks.register("subTaskCheckDependenciesAll") {
//     group = mainBuildGroup
//     description = "Checks the formatting of each of the modules' sub-projects"
//
//     // dependsOn(gradle.includedBuild("modules").task(":applications:app:checkDependencyFormattingProject"))
//     dependsOn(gradle.includedBuild("modules").task(":modules:libraries:list:checkDependencyFormattingProject"))
//     dependsOn(gradle.includedBuild("modules").task(":modules:libraries:utilities:checkDependencyFormattingProject"))
//
// }
//
// val subTaskSourceFileHashingRunAll = tasks.register("subTaskSourceFileHashingRunAll") {
//     group = mainBuildGroup
//     description = "Hashes all of the source files for any sub-projects inside of the modules folder"
//
//     // dependsOn(gradle.includedBuild("modules").task(":applications:app:sourceFileHashingPluginTask"))
//     dependsOn(gradle.includedBuild("modules").task(":modules:libraries:list:sourceFileHashingPluginTask"))
//     dependsOn(gradle.includedBuild("modules").task(":modules:libraries:utilities:sourceFileHashingPluginTask"))
// }
//
// val subTaskPrintRuntimeClasspathAll = tasks.register("subTaskPrintRuntimeClasspathAll") {
//     group = mainBuildGroup
//     description = "Prints the run-time classpath of each of the apps inside of the modules folder"
//
//     dependsOn(gradle.includedBuild("modules").task(":applications:app:printRuntimeClasspath"))
// }
//
// val subTaskDetectClasspathCollisionsAll = tasks.register("subTaskDetectClasspathCollisionsAll") {
//     group = mainBuildGroup
//     description = "Detects classpath collisions for any sub-project in the modules folder"
//
//     // dependsOn(gradle.includedBuild("modules").task(":applications:app:detectClasspathCollisions"))
//     dependsOn(gradle.includedBuild("modules").task(":modules:libraries:list:detectClasspathCollisions"))
//     dependsOn(gradle.includedBuild("modules").task(":modules:libraries:utilities:detectClasspathCollisions"))
// }
//
// val subTaskProjectHealthAll = tasks.register("subTaskProjectHealthAll") {
//     group = mainBuildGroup
//     description = "Runs dependency analysis for all sub-projects and outputs the project health in the build/reports folder"
//
//     // dependsOn(gradle.includedBuild("modules").task(":applications:app:projectHealth"))
//     dependsOn(gradle.includedBuild("modules").task(":modules:libraries:list:projectHealth"))
//     dependsOn(gradle.includedBuild("modules").task(":modules:libraries:utilities:projectHealth"))
// }

