plugins {
    id("kotlin-base-conventions")
    id("application")
}

dependencies {
    implementation(platform("com.nophasenokill.platform:base-platform"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")
}

application {
    // Define the main class for the application.
    mainClass.set("app.AppKt")
}