plugins {
    id("java-platform")
}

group = "com.nophasenokill.platforms"


/*
    Do not use this. This includes the dependencies on the classpath.

    This means the platform can be run as a test - which I'm not sure if we want at this stage
 */

// javaPlatform.allowDependencies()

dependencies {
    constraints {
        runtime("org.junit:junit-bom:5.10.1")
    }
}