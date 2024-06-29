
tasks.register("publishMavenPublicationToMavenRepository") {
    dependsOn(":account:publishMavenPublicationToMavenRepository")
    dependsOn(":person:publishMavenPublicationToMavenRepository")
}

tasks.register("build") {
    dependsOn(":account:build")
    dependsOn(":person:build")
}