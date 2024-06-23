plugins {
    `kotlin-dsl`
}

tasks.register("compileAll") {
    dependsOn(subprojects.map { it.tasks.named("compileJava") })
}
