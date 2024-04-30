plugins {
    id("java-platform")
}

group = "com.nophasenokill.platforms"


// Do not use this. This includes the dependencies
// javaPlatform.allowDependencies()

dependencies {
    constraints {
        api("org.jetbrains.kotlin:kotlin-bom") {
            version {
                strictly("1.9.21")
            }
        }

        api("org.jetbrains.kotlin:kotlin-reflect") {
            version {
                strictly("1.9.21")
            }
        }

        api("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm") {
            version {
                strictly("1.8.0")
            }
        }

        api("org.jetbrains.kotlinx:kotlinx-coroutines-bom") {
            version {
                strictly("1.8.0")
            }
        }

        api("org.slf4j:slf4j-api") {
            version {
                strictly("2.0.12")
            }
        }
        api("org.slf4j:slf4j-simple") {
            version {
                strictly("2.0.12")
            }
        }
    }
}
