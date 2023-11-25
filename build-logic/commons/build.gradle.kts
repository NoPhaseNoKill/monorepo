plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("com.integraboost.platform:plugins-platform"))
    implementation(platform("com.integraboost.platform:test-platform"))

//    constraints {
//        implementation("org.apache.commons:commons-text:1.10.0")
//    }

    testImplementation("org.junit.jupiter:junit-jupiter")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
