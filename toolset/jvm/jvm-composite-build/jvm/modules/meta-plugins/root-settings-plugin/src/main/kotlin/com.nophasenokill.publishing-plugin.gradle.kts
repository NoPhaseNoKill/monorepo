plugins {
    id("com.nophasenokill.java-plugin")
    id("maven-publish")
}

publishing.publications.create<MavenPublication>("mavenJava") {
    from(components["java"])

    /*
        Publish the versions that are the result of the consistent resolution
     */
    versionMapping { allVariants { fromResolutionResult() } }
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven {
        url = uri("${rootProject.projectDir}/local-published-plugins")
    }
}

publishing.repositories {
    val publishedPath = file("${rootDir}/local-published-plugins").path
    maven(providers.environmentVariable("PUBLISHING_REPOSITORY_URL").getOrElse(publishedPath)) {
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType(JavaCompile::class.java).configureEach {
    finalizedBy("publish")
}

group = "com.nophasenokill"
version = "1.0.0-local"
