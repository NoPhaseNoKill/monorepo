

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
    // required to compile the plugin, and hence implicitly relies on the platform being declared
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
    implementation("commons-io:commons-io")
    implementation("commons-codec:commons-codec")
}