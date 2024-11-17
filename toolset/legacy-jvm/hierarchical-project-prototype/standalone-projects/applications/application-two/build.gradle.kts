plugins {
    id("com.nophasenokill.kotlin-plugins.kotlin-plugin-one.convention-plugin-one")
    id("com.nophasenokill.binary-plugins.binary-plugin-one")
    id("com.nophasenokill.binary-plugins.binary-plugin-two")
    id("com.nophasenokill.conventions-plugins-plugin-one")
    id("com.nophasenokill.conventions-plugins-plugin-two")
}

dependencies {
    implementation("com.nophasenokill.binary-plugins:binary-plugin-one:0.1.local-dev")
    implementation("com.nophasenokill.binary-plugins:binary-plugin-two:0.1.local-dev")
}
