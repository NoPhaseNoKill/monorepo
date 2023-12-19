plugins {
    id("java")
}

group = "com.integraboost.myproduct"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))

    }
}


dependencies {
    implementation(platform("com.integraboost.build-logic-platform:product-platform"))

    testImplementation(platform("com.integraboost.build-logic-platform:test-platform"))
    implementation("org.junit.jupiter:junit-jupiter") // might be worth extracting to serparate later
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4") // might be worth extracting to serparate later
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4") // might be worth extracting to serparate later

}

tasks.test {
    useJUnitPlatform()
}
