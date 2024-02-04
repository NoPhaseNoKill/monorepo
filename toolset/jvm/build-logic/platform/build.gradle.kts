

plugins {
    id("java-platform")
    id("dependency-analysis-platform")
}

group = "com.nophasenokill.platform"

javaPlatform.allowDependencies()

dependencies {

    api(platform("org.jetbrains.kotlin:kotlin-bom:1.9.20"))  {
        because("It matches the version of 3.2.0 for spring")
    }

    api(platform("org.junit:junit-bom:5.10.1"))

    api(platform("org.springframework.boot:spring-boot-dependencies:3.2.0"))  {
        because("It matches the version of 1.9.20 for kotlin bom")
    }

    constraints {
        api("com.autonomousapps:dependency-analysis-gradle-plugin:1.29.0")
        api("io.fuchs.gradle.classpath-collision-detector:classpath-collision-detector:0.3")
        api("jakarta.activation:jakarta.activation-api:1.2.2")
        api("javax.activation:activation:1.1.1")
        api("org.apache.commons:commons-text:1.11.0")
        api("org.gradlex:java-ecosystem-capabilities:1.4")
        api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.20")

        configurations.all {
            resolutionStrategy {
                failOnVersionConflict()
                preferProjectModules()

                /*
                    Disable caching so we can detect issues as fast as possible
                 */
                cacheDynamicVersionsFor(0, TimeUnit.MILLISECONDS)
                cacheChangingModulesFor(0, TimeUnit.MILLISECONDS)

            }
        }
    }
}