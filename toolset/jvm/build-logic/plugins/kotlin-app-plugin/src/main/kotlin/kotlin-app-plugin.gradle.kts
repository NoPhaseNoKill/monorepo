plugins {
    id("base-plugin")
    id("application-plugin")
    id("junit-test-plugin")
    id("my-kotlin-plugin")
    id("source-file-hashing-plugin")
    id("capability-conflict-avoidance-plugin")
    id("dependency-analysis-project")
    id("commons-tested-plugin")
    id("greeting-plugin")
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
