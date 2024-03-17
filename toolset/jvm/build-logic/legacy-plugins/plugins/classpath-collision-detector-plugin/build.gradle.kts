

plugins {
    id("java-gradle-plugin")
    `kotlin-dsl`
}

gradlePlugin {
    val classpathCollisionDetector by plugins.creating {
        id = "classpath-collision-detector-plugin"
        implementationClass = "com.nophasenokill.ClasspathCollisionDetectorPlugin"
    }
}

dependencies {
    implementation("commons-io:commons-io:${libs.versions.commonsIo.get()}")
    implementation("commons-codec:commons-codec:${libs.versions.commonsCodec.get()}")
}