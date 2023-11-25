plugins {
    id("java-platform")
}

group = "com.integraboost.platform"

// allow the definition of dependencies to other platforms like the JUnit 5 BOM
javaPlatform.allowDependencies()

dependencies {
    api(platform("org.junit:junit-bom:5.9.3"))

    val junitVersion = "5.10.1"
    api("org.junit.jupiter:junit-jupiter:${junitVersion}")
}
