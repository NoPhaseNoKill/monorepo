plugins {
    kotlin("jvm") version("2.0.20")
    id("org.springframework.boot") version("3.1.5")
    id("io.spring.dependency-management") version("1.1.4")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.arrow-kt:arrow-core:1.2.0")

    runtimeOnly("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}
