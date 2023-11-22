plugins {
    `java-library`
}

dependencies {
    val wiremockVersion = "2.35.0"

    implementation("com.github.tomakehurst:wiremock-jre8:${wiremockVersion}")
}