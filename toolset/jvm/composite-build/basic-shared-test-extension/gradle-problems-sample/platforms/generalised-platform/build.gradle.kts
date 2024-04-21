plugins {
    id("java-platform")
}

group = "com.nophasenokill.platforms"


// Do not use this. This includes the dependencies
// javaPlatform.allowDependencies()

dependencies {
    constraints {
        api("org.jetbrains.kotlin:kotlin-bom:1.9.23") {
            version {
                strictly("1.9.23")
            }
        }
        api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.23") {
            version {
                strictly("1.9.23")
            }
        }
        api("org.jetbrains:annotations:13.0") {
            version {
                strictly("13.0")
            }
        }
        api("org.jetbrains.kotlin:kotlin-stdlib:1.9.23") {
            version {
                strictly("1.9.23")
            }
        }
        api("org.jetbrains.kotlin:kotlin-klib-commonizer-embeddable") {
            api("org.jetbrains.kotlin:kotlin-compiler-embeddable") {
                api("org.jetbrains.kotlin:kotlin-reflect") {
                    version {
                        strictly("1.9.23")
                    }
                }
            }
        }

        api("org.jetbrains.kotlin:kotlin-reflect") {
            version {
                strictly("1.9.23")
            }
        }
        api("org.slf4j:slf4j-api:2.0.12") {
            version {
                strictly("2.0.12")
            }
        }
        api("org.slf4j:slf4j-simple:2.0.12") {
            version {
                strictly("2.0.12")
            }
        }
    }
}