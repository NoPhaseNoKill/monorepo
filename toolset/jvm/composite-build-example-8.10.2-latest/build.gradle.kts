

val publishPlugins = tasks.register("buildPluginJars") {

    /*
        Ensures that any subprojects which ARE a plugin are built first.
        By using this, alongside the root settings.gradle.kts file resolution management,
        we allow for our locally developed plugins to be accessible on the buildscript
        classpath - without the need to publish them manually.

        The advantages of doing this are:
            - We always want our local code, plugins or otherwise, to be using latest
            - The first time the project is pulled down, you do not run into the chicken/egg
            problem, where the settings.gradle.kts is reliant on a subproject, but does
            not know where to fetch this from
            - Using gradle's suggested approach of includedBuilds, we run into limitations
            declared here: https://docs.gradle.org/current/userguide/composite_builds.html#current_limitations_and_future_work
            One of the SIGNIFICANT downsides, is the inability to guarantee parallelization safety.
            However, it also has the problem whereby any publications exposed that are not default,
            will not work. This hampers us from doing things like:

                - Declaring a plugin which uses a kotlin dsl version for our whole monorepo build

                - Using the same dependencies from that build, sharing them through publications,
                and exposing them to our libraries/applications in a nice way. Gradle claims
                that constraints, platforms etc are also a tool used for this purpose - but
                one of the inherent problems is that without a single entry point, unless
                you are VERY careful - and explicitly check almost everything - it's almost
                impossible to NOT goof up dependencies. If you need convincing of this, just see
                the dependency tree for any project which has already tried using platforms/constraints
                to solve this issue. Normally what happens is that the `kotlin-dsl` gets pulled in,
                which may or may not be the same version as our desired kotlin version. After
                it's already loaded, we then trigger subsequent downloads - but it's already
                too late - the plugin version has leaked into the classpath and already
                configured certain things. This can lead to confusion such as: https://github.com/jjohannes/gradle-project-setup-howto/pull/52

            This approach, however, utilizes the mechanism of what an includedBuild gives us (a way of
            declaring resolved, finalized versions, from local builds) but also allows us to do things in parallel.
     */

    subprojects.filter { it.plugins.hasPlugin("java-gradle-plugin") }.forEach { pluginProject ->
        dependsOn(pluginProject.tasks.named("jar"))
        finalizedBy(pluginProject.tasks.named("publish"))
    }
}

val buildAllSubprojects = tasks.register("buildAllSubprojects") {
    /*
        Ensures that any subprojects which are NOT a plugin (aka a library or application)
        are built using the latest locally developed plugins.
     */
    subprojects.filterNot { it.plugins.hasPlugin("java-gradle-plugin") }.forEach { libProject ->
        dependsOn(libProject.tasks.withType<JavaCompile>())
        dependsOn(libProject.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>())
    }

    mustRunAfter(publishPlugins)
}

val buildAllPlugins = tasks.register("buildAllPlugins") {
    dependsOn(publishPlugins)
    finalizedBy(buildAllSubprojects)
}

tasks.register("buildAll") {
    dependsOn(buildAllPlugins)
    dependsOn(buildAllSubprojects)
}
