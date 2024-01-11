plugins {
    id("java-platform")
}

group = "com.nophasenokill.platform"

// allow the definition of dependencies to other platforms like the Spring Boot BOM
javaPlatform.allowDependencies()

dependencies {
    api(platform("org.springframework.boot:spring-boot-dependencies:3.2.1"))

    api("org.apache.commons:commons-text:1.10.0")
}
