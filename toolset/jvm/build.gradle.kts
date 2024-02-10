
group = "com.nophasenokill.jvm"

tasks.register("testAll") {
    group = "verification"
    description = "Run all feature tests"

    dependsOn(gradle.includedBuild("modules").task(":applications:app:test"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:list:test"))
    dependsOn(gradle.includedBuild("modules").task(":libraries:utilities:test"))

}