plugins {
    id("java")
}


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    implementation(platform("com.nophasenokill.platform:product-platform"))
    testImplementation(platform("com.nophasenokill.platform:test-platform"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")


    implementation("org.apache.commons:commons-text")
}

tasks.test {
    useJUnitPlatform()
}
