rootProject.name = "libraries"

includeBuild("../plugins")

include("my-greeting-lib")

gradle.lifecycle.beforeProject {
    tasks.register("buildAll") {
        dependsOn(":my-greeting-lib:build")
    }
}
