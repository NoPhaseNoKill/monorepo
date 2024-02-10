plugins {

    id("commons-plugin")

    id("org.jetbrains.kotlin.jvm")
    id("application")
    // TODO THIS NEEDS TO BE FIXED - BUT WORKS - also clean up the build file of this
    // id("capability-conflict-avoidance-plugin")
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

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // applies test projects
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

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

tasks.test {
    dependsOn(printRuntimeClasspathTask)
}