plugins {
    `kotlin-dsl` version("4.3.0") apply false
}

tasks.register("buildAllComposite") {
    group = "verification"
    description = "Builds all projects, which includes assembling them and running all checks (tests/functional tests)"

    dependsOn(gradle.includedBuild("applications").task(":application-one:build"))
    dependsOn(gradle.includedBuild("meta-plugins").task(":meta-plugin-one:build"))
    dependsOn(gradle.includedBuild("standalone-plugins").task(":standalone-plugin-one:build"))
}