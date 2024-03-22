plugins {
    id("java-platform")
}

group = "com.nophasenokill.platform"


javaPlatform.allowDependencies()

dependencies {
    api(platform("org.junit:junit-bom:5.10.1"))
}
