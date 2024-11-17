

plugins {
    id("org.gradle.test-retry")
}

if (System.getenv().containsKey("CI")) {
    tasks.withType<Test>().configureEach {
        retry {
            maxRetries.set(2)
        }
    }
}
