plugins {

    // TODO THIS NEEDS TO BE FIXED - BUT WORKS - also clean up the build file of this
    // Means the build-health-reports doesnt fail when running root task buildHealth

    id("commons-plugin")
    // id("capability-conflict-avoidance-plugin")

    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    // id("dependency-analysis-project")
    // id("greeting-plugin")
}

// Configure common dependencies for all projects
dependencies {
    // enforces that versions from each of the boms are used
    implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
    implementation(enforcedPlatform("org.jetbrains.kotlin:kotlin-bom"))
    testImplementation(enforcedPlatform("org.junit:junit-bom"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // applies test projects
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}