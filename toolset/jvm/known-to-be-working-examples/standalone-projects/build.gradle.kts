
plugins {
    `kotlin-dsl`
}

tasks.register("buildAll") {
    dependsOn(":application-one:build")
    dependsOn(":library-one:build")
}

tasks.register("testAll") {
    dependsOn(":application-one:test")
    dependsOn(":library-one:test")
}