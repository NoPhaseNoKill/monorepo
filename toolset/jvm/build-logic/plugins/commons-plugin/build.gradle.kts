

plugins {
    `kotlin-dsl`
}

dependencies {
    /*
        This allow us to declare non-versioned dependencies inside of each project.
        It also ensures that transitive dependencies are pinned to our versions
        from the platform.
     */
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin")
}