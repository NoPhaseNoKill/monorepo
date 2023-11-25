plugins {
    id("java")
    id("com.integraboost.jacoco")
    //TODO add test plugin
}

group = "com.integraboost"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(19)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("com.integraboost.platform:product-platform"))

    testImplementation(platform("com.integraboost.platform:test-platform"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

//subprojects {
//    apply {
//        plugin("java")
//    }
//
//    dependencies {
//        implementation(platform("com.integraboost.platform:product-platform"))
//
//        testImplementation(platform("com.integraboost.platform:test-platform"))
//        testImplementation("org.junit.jupiter:junit-jupiter")
//        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
//    }
//
//    buildDir = File("${rootProject.buildDir}/${project.name}")
//
//    tasks.withType<Test> {
//        useJUnitPlatform()
//    }
//}
