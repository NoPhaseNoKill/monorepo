
/*
    DO NOT DECLARE BUILD SCRIPT HERE
 */


tasks.register("build") {
    dependsOn(gradle.includedBuilds.map { it.task(":build") })
}
