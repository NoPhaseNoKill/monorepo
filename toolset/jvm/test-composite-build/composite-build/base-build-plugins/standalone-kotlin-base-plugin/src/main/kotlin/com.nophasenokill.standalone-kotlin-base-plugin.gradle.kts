plugins {
    kotlin("jvm")
    `maven-publish`
}

/*
    Configures this for the underlying project that uses the plugin.
    These coordinates allow for us to build into '$rootDir/local-repo',
    which in turn allow us to do the following:

        i) Use plugins at the top level of the composite build of a rootProject
        ii) Create plugins within the composite build of a rootProject
        iii) Have them remain on the test classpath,so that we can
        make them infinitely easier to test
 */
project.apply {
    group = "com.nophasenokill"
    version = "1.0.0-local-dev"
}

dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${embeddedKotlinVersion}")
    implementation(gradleApi())
    testImplementation(gradleTestKit())


    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

publishing {
    repositories {
        maven {
            url = uri("${rootProject.projectDir}/local-repo")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.build {
    finalizedBy("publish")
}
