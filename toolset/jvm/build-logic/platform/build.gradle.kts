

plugins {
    id("java-platform")
    id("dependency-analysis-platform")
}

group = "com.nophasenokill.platform"


// javaPlatform.allowDependencies()

// configurations.all {
//     resolutionStrategy {
//         // force("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3")
//         // force("org.jetbrains.kotlin:kotlin-reflect:1.9.20")
//         failOnVersionConflict()
//     }
// }

// TODO FIGURE THIS OUT

dependencies {

    // api(platform("org.junit:junit-bom:5.10.1"))
    //
    // api(platform("org.springframework.boot:spring-boot-dependencies:3.2.0"))  {
    //     because("It matches the version of 1.9.20 for kotlin bom")
    // }
    //
    // api(platform("org.jetbrains.kotlin:kotlin-bom:1.9.20"))  {
    //     because("It matches the version of 3.2.0 for spring")
    // }


    constraints {

        // api(project(":utilities"))
        // api(project(":lib"))
        // api(project(":utils"))


        api("org.junit:junit-bom:5.10.1")
        // api("org.junit.jupiter:junit-jupiter:5.10.1") {
        //     because("Aligns with the junit bom")
        // }

        api("org.springframework.boot:spring-boot-dependencies:3.2.0")  {
            because("It matches the version of 1.9.20 for kotlin bom")
        }

        api("org.jetbrains.kotlin:kotlin-bom:1.9.20")  {
            because("It matches the version of 3.2.0 for spring")
        }

        api("com.autonomousapps:dependency-analysis-gradle-plugin:1.29.0")
        api("io.fuchs.gradle.classpath-collision-detector:classpath-collision-detector:0.3")
        api("jakarta.activation:jakarta.activation-api:1.2.2")
        api("javax.activation:activation:1.1.1")
        api("org.apache.commons:commons-text:1.11.0")
        api("org.gradlex:java-ecosystem-capabilities:1.4")
        api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.20")
        api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.20")
        api("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")

    }
}