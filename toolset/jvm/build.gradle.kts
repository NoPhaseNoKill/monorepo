
plugins {
    `kotlin-dsl` apply false
    id("com.nophasenokill.hash-source-plugin")
}

val publishDeps = tasks.register("publishDeps") {
    gradle.includedBuilds.map {
        dependsOn(it.task(":publishMavenPublicationToMavenRepository"))
    }
    dependsOn("known-to-be-working-examples:plugins:basic-plugin:publishMavenPublicationToMavenRepository")
    dependsOn("known-to-be-working-examples:plugins:extended-plugin:publishMavenPublicationToMavenRepository")
}

tasks.register("buildAll") {
    mustRunAfter(publishDeps)

    gradle.includedBuilds.map { dependsOn(it.task(":build")) }

    dependsOn("known-to-be-working-examples:plugins:basic-plugin:build")
    dependsOn("known-to-be-working-examples:plugins:extended-plugin:build")
}