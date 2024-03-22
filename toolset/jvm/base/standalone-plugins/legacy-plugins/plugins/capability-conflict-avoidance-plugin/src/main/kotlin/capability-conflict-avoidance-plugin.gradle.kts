/*

    This is a plugin which is a culmination of one or more plugins that aim to detect
    and alert developers to capability conflicts. Ideally this would also automatically
    fix everything for you, but this is not currently supported yet.

    A capability conflict is where we have dependencies that expose the same 'capability' (publicly exposed
    methods/functions/properties from a java class), that are shared across two or more dependencies.

    Current behaviour:
        - Detects the resulting conflict

    Future plans may include:
        - Fixing the dependency in the build script file by replacing it (and any potential imports)
        - Outputting this to the user to signify what was done

    Example:

    Consider the following dependency declaration:

        implementation("javax.activation:activation:1.1.1")
        implementation("javax.activation:activation:1.1")
        implementation("jakarta.activation:jakarta.activation-api:1.2.2")

    1. javax.activation:activation is actually the same dependency as jakarta.activation:jakarta.activation-api.
        The project was merely renamed.

    2. Because of the ordering of these, it will put javax.activation:activation:1.1.1 on the classpath

    3. This may result in runtime errors/hard to debug problems, where we thought it was using
        jakarta.activation:jakarta.activation-api:1.2.2, but it's actually using the equivalent of 1.1.1

    The classpath comparisons for what you actually want, versus what actually happens is:

    What you expect to happen in an ideal world:

        > Task :modules:applications:app:run
        file:/home/REDACTED/.gradle/caches/modules-2/files-2.1/jakarta.activation/jakarta.activation-api/1.2.2/99f53adba383cb1bf7c3862844488574b559621f/jakarta.activation-api-1.2.2.jar

    What actually happens:

        > Task :modules:applications:app:run
        file:/home/REDACTED/.gradle/caches/modules-2/files-2.1/javax.activation/activation/1.1.1/485de3a253e23f645037828c07f1d7f1af40763a/activation-1.1.1.jar
 */
plugins {
    id("classpath-collision-detector-plugin") // Allow detection of collisions through running detectClasspathCollisions task
    /*
        While you may be tempted to use this,

            id("org.gradlex.java-ecosystem-capabilities")

         this actually obfuscates the underlying problem by working around it. Instead, we want to flag
         the issue so that we can fix the underlying dependency declaration, hence removing the need to do
         workarounds or anything that might add time to a build scripts configuration time OR even a plugin
         which adds unnecessary transitive dependencies. This conforms to the premise of keeping this project
         as lean as possible.

         Note: The fundamental way of solving this problem is that we need to firstly detect, and then
         remove any unnecessary dependencies. This is done by removing the unnecessary ones, and changing
         any files dependent on those.
     */
}