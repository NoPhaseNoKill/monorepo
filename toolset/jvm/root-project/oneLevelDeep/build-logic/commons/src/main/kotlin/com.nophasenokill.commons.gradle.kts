plugins {
    id("java")
    id("com.nophasenokill.jacoco")
}

group = "com.nophasenokill.sensibledefaults"

dependencies {
    implementation(platform("com.nophasenokill.platform:product-platform"))

    testImplementation(platform("com.nophasenokill.platform:test-platform"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
