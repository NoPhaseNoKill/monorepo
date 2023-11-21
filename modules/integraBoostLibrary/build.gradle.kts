
plugins {
    id("com.integraboost.kotlin-common-conventions")
}

dependencies {
    val wiremockVersion = "2.35.0"
    val junitVersion = "5.9.3"

    implementation("com.github.tomakehurst:wiremock-jre8:${wiremockVersion}")
    implementation("org.junit.jupiter:junit-jupiter:${junitVersion}")
}