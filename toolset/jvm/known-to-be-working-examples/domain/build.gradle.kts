
plugins {
    /*
        id("com.nophasenokill.build-service-warning-fix-plugin")

        Needs to go in each of the subprojects, otherwise the warning/error remains
     */
}

tasks.register("publishMavenPublicationToMavenRepository") {
    dependsOn(":account:publishMavenPublicationToMavenRepository")
    dependsOn(":person:publishMavenPublicationToMavenRepository")
}

tasks.register("build") {
    dependsOn(":account:build")
    dependsOn(":person:build")
}