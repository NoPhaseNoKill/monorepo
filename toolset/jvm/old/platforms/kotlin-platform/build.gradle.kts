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
    api(platform("org.jetbrains.kotlin:kotlin-bom:1.9.10"))

    constraints {
        api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.10")
    }
}