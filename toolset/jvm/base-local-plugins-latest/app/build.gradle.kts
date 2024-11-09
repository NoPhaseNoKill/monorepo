
plugins {
    java
    application
    id("org.sample.number-utils")
}

application {
    mainClass = "org.sample.myapp.Main"
}

val sharedConfiguration: Configuration by configurations.creating {
    isCanBeConsumed = false
}

application {
    mainClass = "org.sample.myapp.Main"
}

dependencies {
    sharedConfiguration("org.sample:anonymous-library:1.0")
    implementation("org.sample:anonymous-library:1.0")
}



