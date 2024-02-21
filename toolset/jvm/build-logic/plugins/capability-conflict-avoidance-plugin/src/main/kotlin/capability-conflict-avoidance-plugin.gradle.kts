import io.fuchs.gradle.collisiondetector.DetectCollisionsTask

/*
    Prevents issues under the following circumstances:

    1. Due to transitive dependencies (or manually declaring), you effectively end up with these dependencies:

        implementation("javax.activation:activation:1.1.1")
        implementation("javax.activation:activation:1.1")
        implementation("jakarta.activation:jakarta.activation-api:1.2.2")

    2. Because java handles the dependency it first sees in the classpath AND it has no concept of different names
        where a dependency may now have been renamed, depending on the ordering the resulting version may not be
        expected
    3. The above example would normally have resolved the transitive dependencies (in an ideal world) to
        have implementation("javax.activation:activation:1.2.2") -> because this is actually the latest dependency.


    With this:

    > Task :modules:applications:app1:run
        file:/home/REDACTED/.gradle/caches/modules-2/files-2.1/jakarta.activation/jakarta.activation-api/1.2.2/99f53adba383cb1bf7c3862844488574b559621f/jakarta.activation-api-1.2.2.jar


    Without this:
    > Task :modules:applications:app1:run
    file:/home/REDACTED/.gradle/caches/modules-2/files-2.1/javax.activation/activation/1.1.1/485de3a253e23f645037828c07f1d7f1af40763a/activation-1.1.1.jar

    For more details, see:
    https://github.com/REPLicated/classpath-collision-detector
    https://github.com/gradlex-org/java-ecosystem-capabilities

 */
plugins {
    id("io.fuchs.gradle.classpath-collision-detector")
    id("org.gradlex.java-ecosystem-capabilities")
}

tasks.named("detectCollisions", DetectCollisionsTask::class.java).configure {
    collisionFilter {
        exclude("**.properties")
    }
}