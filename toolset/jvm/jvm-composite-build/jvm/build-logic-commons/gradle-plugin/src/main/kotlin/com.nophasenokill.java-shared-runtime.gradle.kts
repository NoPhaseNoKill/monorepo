import com.nophasenokill.commons.configureJavaToolChain

plugins {
    `java-library`
    groovy

    id("com.nophasenokill.ci-reporting")
    id("com.nophasenokill.code-quality")
    id("com.nophasenokill.module-jar")
    id("com.nophasenokill.repositories")
    id("com.nophasenokill.reproducible-archives")
    id("com.nophasenokill.private-javadoc")
}

description = "A plugin that sets up a Java code that is shared between build-logic and runtime"

java {
    configureJavaToolChain()
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useSpock()
            dependencies {
                implementation(platform("gradlebuild:build-platform"))
            }
        }
    }
}
