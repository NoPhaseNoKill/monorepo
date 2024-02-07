plugins {
    // TODO THIS NEEDS TO BE FIXED - BUT WORKS
    id("commons-plugin")
    // id("capability-conflict-avoidance-plugin")
    id("org.jetbrains.kotlin.jvm")
    id("application")
    // id("dependency-analysis-project")
}

application {
    mainClass.set("com.nophasenokill.app.AppKt")
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
//     // // applies basic deps
//     // implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
//     // testImplementation(enforcedPlatform("com.nophasenokill.platform:platform"))
//     // implementation(enforcedPlatform("com.nophasenokill.platform:platform"))
//     // testImplementation(enforcedPlatform("com.nophasenokill.platform:platform"))
// }

/*
    Should display something like:
        > Task :app:printRuntimeClasspath
              |------> about to be executed

              [EVENT] Checking classpath capability conflict avoidance...

              Classpath correctly applied capability conflict avoidance

                  Contained:
                     - 'jakarta.activation/jakarta.activation-api/1.2.2'

                  Did not contain:
                     - 'javax.activation:activation:1.1.1'
                     - 'javax.activation:activation:1.1'

              |------> was executed

        > Task :utilities:compileKotlin UP-TO-DATE
 */
val printRuntimeClasspathTask = tasks.register("printRuntimeClasspath") {
    doLast {
        val sourceSets = sourceSets["main"].runtimeClasspath.asPath
        val checkingEvent = "[EVENT] Checking classpath capability conflict avoidance..."

        val capabilityConflictsThatShouldBeAvoided = "javax.activation:activation:1.1"
        val expectedCapability = "jakarta.activation/jakarta.activation-api/1.2.2"

        if(sourceSets.contains(capabilityConflictsThatShouldBeAvoided) || !sourceSets.contains(expectedCapability)) {
            throw GradleException("""      |
          | ${checkingEvent}
          | 
          |     Classpath should contain: 
          |      - '${expectedCapability}'
          |     Classpath should NOT contain:
          |      - '${capabilityConflictsThatShouldBeAvoided}.1'
          |      - '${capabilityConflictsThatShouldBeAvoided}'
          |     Classpath was: ${sourceSets}
            """)
        } else {
            println("""      |
      | ${checkingEvent}
      | 
      |     Classpath correctly applied capability conflict avoidance
      |                      
      |        Contained:
      |           - '${expectedCapability}'
      |             
      |        Did not contain:
      |           - '${capabilityConflictsThatShouldBeAvoided}.1'
      |           - '${capabilityConflictsThatShouldBeAvoided}'
      |           """)
        }
    }
}

// tasks.named("run") {
//     dependsOn(printRuntimeClasspathTask)
// }
//
// tasks.testAll {
//     dependsOn(printRuntimeClasspathTask)
// }

tasks.test {
    dependsOn(printRuntimeClasspathTask)
}