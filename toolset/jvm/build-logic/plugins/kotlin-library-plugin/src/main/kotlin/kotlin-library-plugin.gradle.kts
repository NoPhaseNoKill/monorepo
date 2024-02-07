plugins {

    //TODO THIS NEEDS TO BE FIXED - BUT WORKS
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

    // // applies test projects
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// dependencies {
//     // applies basic deps
//     implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
//     testImplementation(enforcedPlatform("com.nophasenokill.platform:platform"))
// }

// dependencies {
    // api("org.jetbrains.kotlin:kotlin-stdlib")
    // implementation(kotlin("stdlib"))
// }