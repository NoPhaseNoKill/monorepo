plugins {
    id("org.jetbrains.kotlin.jvm")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    constraints {
        implementation("org.apache.commons:commons-text:1.10.0")
    }
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}