plugins {
    `kotlin-dsl`
    `java-library`
}

dependencies {
    implementation(platform("com.integraboost.platform:plugins-platform"))

    implementation(projects.commons)
}
