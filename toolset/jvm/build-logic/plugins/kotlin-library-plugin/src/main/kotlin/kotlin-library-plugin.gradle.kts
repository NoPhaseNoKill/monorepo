plugins {
    id("commons-plugin")
    id("capability-conflict-avoidance-plugin")
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("dependency-analysis-project")
    id("greeting-plugin")
}

// Configure common dependencies for all projects
dependencies {
    // enforces that versions from each of the boms are used
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
    implementation(enforcedPlatform("org.jetbrains.kotlin:kotlin-bom"))
    testImplementation(enforcedPlatform("org.junit:junit-bom"))

    /*
        adds kotlin to the project. jdk8 ensures that when we set kotlin.stdlib.default.dependency=false
        that our apps/libraries still get kotlin
        DO NOT USE:
            - kotlin("stdlib")
     */
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // applies test projects
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}