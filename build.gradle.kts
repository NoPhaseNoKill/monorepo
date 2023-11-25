

//tasks.withType<Test> {
//    useJUnitPlatform()
//}

// example of how the composite publishes each of builds (components)
tasks.register("test") {
    dependsOn(gradle.includedBuilds.map { it.task(":test") })
}