
plugins {
    `kotlin-dsl`
}

dependencies {

    implementation(project(":base-plugin"))
    implementation(project(":library-plugin"))
    implementation(project(":junit-test-plugin"))
    implementation(project(":my-kotlin-plugin"))

}