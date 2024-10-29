plugins {
    kotlin("jvm") version "2.0.20"
    id("application")
    id("org.sample.greeting").version("1.0-SNAPSHOT")
    id("org.sample.secondary").version("1.0-SNAPSHOT")
}

repositories {
    gradlePluginPortal()
}

greeting {
    who = "Bob"
}

greetingSecondary {
    who = "Bob2"
}
