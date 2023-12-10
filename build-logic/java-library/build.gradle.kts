plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform("com.integraboost.build-logic-platform:plugins-platform"))

    implementation(project(":commons"))
}
