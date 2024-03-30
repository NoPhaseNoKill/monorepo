package com.nophasenokill

import org.gradle.api.Action
import org.gradle.api.logging.Logger
import org.gradle.api.tasks.testing.Test
import java.io.Serializable
import java.net.URL
import java.net.URLClassLoader

class GradleDaemonClasspathEnhancer(
    // private val classNames: List<List<String>>
) : Action<Test> {

    override fun execute(task: Test) {
        val logger = task.logger
        // enhanceGradleDaemonClasspath(logger, task, classNames)
        enhanceGradleDaemonClasspath(logger, task)
    }

    // private fun enhanceGradleDaemonClasspath(logger: Logger, task: Test, classNames: List<List<String>>) {
    private fun enhanceGradleDaemonClasspath(logger: Logger, task: Test) {
        // try {
        //     val testClasspath = getTestClasspath(task)
        //     val classpath = classNames.flatMap {
        //         findJarsForClasses(testClasspath, it)
        //     }
        //
        //     logger.debug("Test classpath:")
        //     for (url in testClasspath) {
        //         logger.debug(url.toString())
        //     }
        //     logger.debug("Reduced classpath:")
        //     for (url in classpath) {
        //         logger.debug(url.toString())
        //     }
        //     logger.debug("End of classpath")
        //
        //     if (classpath.isNotEmpty()) {
        //         val classLoader = getDaemonClassLoader()
        //         addClasspath(classLoader, classpath)
        //     }
        // }
        // catch (exception: Throwable) {
        //     logger.error("Unable to enhance Gradle Daemon by $classNames", exception)
        // }
    }

    // private fun getDaemonClassLoader(): URLClassLoader {
    //     val daemonMainClass = Class.forName("org.gradle.launcher.daemon.bootstrap.DaemonMain")
    //     val classLoader = daemonMainClass.classLoader
    //     if (classLoader is URLClassLoader) {
    //         return classLoader
    //     }
    //     throw IllegalStateException("Unsupported Gradle daemon class loader: $classLoader")
    // }
    //
    // private fun getTestClasspath(task: Test): List<URL> {
    //     return task.classpath.files.map { it.toURI().toURL() }
    // }
    //
    // /**
    //  * Finds reduced classpath for classes. The [classNames] parameter list
    //  * should contain expected class and all its dependencies. Therefore,
    //  * function returns empty list if any of classes wasn't found.
    //  */
    // private fun findJarsForClasses(classpath: List<URL>, classNames: List<String>): List<URL> {
    //     val foundClassNames = HashSet<String>()
    //     val reducedClasspath = ArrayList<URL>()
    //     for (url in classpath) {
    //         URLClassLoader(arrayOf(url)).use { classLoader ->
    //             for (className in classNames) {
    //                 if (hasClass(classLoader, className)) {
    //                     foundClassNames.add(className)
    //                     reducedClasspath.add(url)
    //                     break
    //                 }
    //             }
    //         }
    //     }
    //     return if (foundClassNames.size == classNames.size) reducedClasspath else emptyList()
    // }
    //
    // private fun hasClass(classLoader: URLClassLoader, className: String): Boolean {
    //     val classFile = className.replace(".", "/") + ".class"
    //     return classLoader.findResource(classFile) != null
    // }
    //
    // private fun addClasspath(classLoader: URLClassLoader, classpath: Iterable<URL>) {
    //     for (url in classpath) {
    //         // Ensures addUrl method is accessible in kotlin
    //         val method = URLClassLoader::class.java.getDeclaredMethod("addURL", URL::class.java)
    //         method.isAccessible = true
    //         method.invoke(classLoader, url)
    //     }
    // }
}

// jps -v
// 20608 GradleDaemon --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED --add-opens=java.prefs/java.util.prefs=ALL-UNNAMED --add-opens=java.base/java.nio.charset=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED -XX:MaxMetaspaceSize=384m -XX:+HeapDumpOnOutOfMemoryError -Xms256m -Xmx512m -Dfile.encoding=UTF-8 -Duser.country=AU -Duser.language=en -Duser.variant -javaagent:/home/tomga/.gradle/wrapper/dists/gradle-8.5-bin/5t9huq95ubn472n8rpzujfbqh/gradle-8.5/lib/agents/gradle-instrumentation-agent-8.5.jar
// 28131 KotlinCompileDaemon -Djava.awt.headless=true -D$java.rmi.server.hostname=127.0.0.1 -Xmx700m -Dkotlin.incremental.compilation=true -Dkotlin.incremental.compilation.js=true -ea --add-exports=java.base/sun.nio.ch=ALL-UNNAMED
// 23076 GradleDaemon -XX:MaxMetaspaceSize=384m -XX:+HeapDumpOnOutOfMemoryError --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED --add-opens=java.prefs/java.util.prefs=ALL-UNNAMED --add-opens=java.base/java.nio.charset=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED -Xmx2g -Dfile.encoding=UTF-8 -Duser.country=AU -Duser.language=en -Duser.variant -javaagent:/home/tomga/.gradle/wrapper/dists/gradle-8.6-bin/afr5mpiioh2wthjmwnkmdsd5w/gradle-8.6/lib/agents/gradle-instrumentation-agent-8.6.jar
// 10740 GradleDaemon -XX:MaxMetaspaceSize=384m -XX:+HeapDumpOnOutOfMemoryError --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED --add-opens=java.prefs/java.util.prefs=ALL-UNNAMED --add-opens=java.base/java.nio.charset=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED -Xmx2g -Dfile.encoding=UTF-8 -Duser.country=AU -Duser.language=en -Duser.variant -javaagent:/home/tomga/.gradle/wrapper/dists/gradle-8.6-bin/afr5mpiioh2wthjmwnkmdsd5w/gradle-8.6/lib/agents/gradle-instrumentation-agent-8.6.jar
// 2324  -Djna.nounpack=true -Djna.nosys=true -Djna.noclasspath=true -Djna.library.path=/tmp/.mount_jetbraVDpy1F -Djna.boot.library.path=/tmp/.mount_jetbraVDpy1F -Dskiko.library.path=/tmp/.mount_jetbraVDpy1F -Xmx160m -Xms8m -Xss384k -XX:+UnlockExperimentalVMOptions -XX:+CreateCoredumpOnCrash -XX:MetaspaceSize=16m -XX:MinMetaspaceFreeRatio=10 -XX:MaxMetaspaceFreeRatio=10 -XX:+UseCompressedOops -XX:+UseCompressedClassPointers -XX:+UseSerialGC -XX:MinHeapFreeRatio=10 -XX:MaxHeapFreeRatio=10 -XX:-ShrinkHeapInSteps -Djdk.lang.processReaperUseDefaultStackSize=true --add-opens=java.desktop/sun.awt=ALL-UNNAMED --add-opens=java.desktop/sun.awt.resources=ALL-UNNAMED vfprintf exit abort -DTOOLBOX_VERSION=2.2.2.20062 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/home/tomga/.local/share/JetBrains/Toolbox/logs/toolbox-oom-2324.hprof -XX:ErrorFile=/home/tomga/.local/share/JetBrains/Toolbox/logs/toolbox-error-2324.log
// 30853 GradleDaemon -XX:MaxMetaspaceSize=384m -XX:+HeapDumpOnOutOfMemoryError --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED -agentlib:jdwp=transport=dt_socket,server=n,suspend=y,address=127.0.0.1:36887 --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED --add-opens=java.prefs/java.util.prefs=ALL-UNNAMED --add-opens=java.base/java.nio.charset=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED -Xmx2g -Dfile.encoding=UTF-8 -Duser.country=AU -Duser.language=en -Duser.variant -javaagent:/home/tomga/.gradle/wrapper/dists/gradle-8.6-bin/afr5mpiioh2wthjmwnkmdsd5w/gradle-8.6/lib/agents/gradle-instrumentation-agent-8.6.jar
// 20295 Main -XX:ErrorFile=/home/tomga/java_error_in_idea_%p.log -XX:HeapDumpPath=/home/tomga/java_error_in_idea_.hprof -Xms128m -Xmx2048m -XX:ReservedCodeCacheSize=512m -XX:+UseG1GC -XX:SoftRefLRUPolicyMSPerMB=50 -XX:CICompilerCount=2 -XX:+HeapDumpOnOutOfMemoryError -XX:-OmitStackTraceInFastThrow -XX:+IgnoreUnrecognizedVMOptions -XX:CompileCommand=exclude,com/intellij/openapi/vfs/impl/FilePartNodeRoot,trieDescend -ea -Dsun.io.useCanonCaches=false -Dsun.java2d.metal=true -Djbr.catch.SIGABRT=true -Djdk.http.auth.tunneling.disabledSchemes="" -Djdk.attach.allowAttachSelf=true -Djdk.module.illegalAccess.silent=true -Dkotlinx.coroutines.debug=off -Dsun.tools.attach.tmp.only=true -Dawt.lock.fair=true -Xmx4096m -Dide.managed.by.toolbox=/home/tomga/.local/share/JetBrains/Toolbox/bin/jetbrains-toolbox -Dtoolbox.notification.token=9ce55a41-4b7e-4c6f-b6e1-8911394e9095 -Dtoolbox.notification.portFile=/home/tomga/.cache/JetBrains/Toolbox/ports/1781205792.port -Djb.vmOptionsFile=/home/tomga/.config/JetBrains/IntelliJIdea2023.3/idea64.v
// 20954 RemoteMavenServer36 -Djava.awt.headless=true -Dmaven.defaultProjectBuilder.disableGlobalModelCache=true -Didea.version=2023.3.2 -Didea.maven.embedder.version=3.9.5 -Xmx768m -Dmaven.ext.class.path=/home/tomga/.local/share/JetBrains/Toolbox/apps/intellij-idea-ultimate/plugins/maven/lib/maven-event-listener.jar -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
// 31115 Jps -Dapplication.home=/home/tomga/.sdkman/candidates/java/21.0.2-ms -Xms8m -Djdk.module.main=jdk.jcmd
// 20813 KotlinCompileDaemon -Djava.awt.headless=true -D$java.rmi.server.hostname=127.0.0.1 -Xmx2g -XX:MaxMetaspaceSize=384m -Dkotlin.environment.keepalive -ea --add-exports=java.base/sun.nio.ch=ALL-UNNAMED