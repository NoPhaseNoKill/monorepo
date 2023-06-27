plugins {
    `java-library`
    kotlin("jvm")
}

dependencies {
    implementation("com.github.tomakehurst:wiremock-jre8:2.27.2")
    implementation("org.junit.jupiter:junit-jupiter:5.7.2")
}