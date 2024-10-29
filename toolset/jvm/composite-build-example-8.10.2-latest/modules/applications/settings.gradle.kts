rootProject.name = "applications"

includeBuild("../plugins")
includeBuild("../libraries")

include("my-greeting-app")

gradle.lifecycle.beforeProject {
    tasks.register("buildAll") {
        dependsOn(":my-greeting-app:build")
    }
}
