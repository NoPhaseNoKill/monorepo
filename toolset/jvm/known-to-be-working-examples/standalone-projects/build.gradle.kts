
tasks.register("build") {
    dependsOn(":application-one:build")
    dependsOn(":library-one:build")
}

tasks.register("test") {
    dependsOn(":application-one:test")
    dependsOn(":library-one:test")
}