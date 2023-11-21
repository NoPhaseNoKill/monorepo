plugins {
    id("com.integraboost.kotlin-common-conventions")

    val springBootVersion = "3.1.5"
    val springDepManagementVersion = "1.1.4"

    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version springDepManagementVersion
}

dependencies {
    implementation(project(":modules:integraBoostLibrary"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    runtimeOnly("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.arrow-kt:arrow-core:1.2.0")
}