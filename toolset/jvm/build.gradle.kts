
plugins {
    `kotlin-dsl` apply false
}


tasks.register("buildAll") {
    mustRunAfter(publishDeps)
    gradle.includedBuilds.map { dependsOn(it.task(":build")) }
}

tasks.register("buildAllStandalone") {
    dependsOn(gradle.includedBuild("standalone-projects").task(":build"))
}

tasks.register("testAllStandalone") {
    dependsOn(gradle.includedBuild("standalone-projects").task(":test"))
}

/*
    Task used to troubleshoot how the published plugins, and any local dependencies they have, will look
 */
val publishDeps = tasks.register("publishDeps") {
    gradle.includedBuilds
        .map {
            if(it.name == "standalone-projects") {
                //do nothing
            } else {
                /*
                    publish anything that our applications/libraries may need for their plugins

                    ie any plugin, any library that a plugin relies on
                 */
                dependsOn(it.task(":publishMavenPublicationToMavenRepository"))
            }
    }
}