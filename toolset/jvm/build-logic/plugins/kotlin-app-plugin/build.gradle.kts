plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(project(":base-plugin"))

    // includes junit plugin which includes test deps etc
    implementation(project(":application-plugin"))

    implementation(project(":my-kotlin-plugin"))
}
