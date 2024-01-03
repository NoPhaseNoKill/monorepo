plugins {
    id("java-platform")
}

group = "integraboost.platform"

/*
    Allow the definition of dependencies to other platforms like the JUnit 5 BOM
    Example:

    ****
        dependencies {

            // notice how these are not inside constraints
            api(platform("org.junit:junit-bom:5.7.1"))
            api(platform("org.springframework.boot:spring-boot-dependencies:2.7.8"))

            constraints {
                .....
            }
        }
    ****

 */
javaPlatform.allowDependencies()

dependencies {
    constraints {
        api("com.autonomousapps:dependency-analysis-gradle-plugin:1.28.0")
        api("io.fuchs.gradle.classpath-collision-detector:classpath-collision-detector:0.3")
        api("org.gradlex:java-ecosystem-capabilities:1.3.1")

        api("jakarta.activation:jakarta.activation-api:1.2.2")
        api("javax.activation:activation:1.1.1")
    }
}
