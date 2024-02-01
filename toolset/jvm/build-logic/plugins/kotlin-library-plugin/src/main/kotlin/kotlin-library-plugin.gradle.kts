plugins {
    id("commons-plugin")
    id("java-library")
    id("dependency-analysis-project")
    id("greeting-plugin")
}

dependencies {
    /*
       This specifically needs to be api, otherwise list/utilities projectHealth tasks fails with:

       Existing dependencies which should be modified to be as indicated:
       api("org.jetbrains.kotlin:kotlin-stdlib:1.9.20") (was implementation)
    */
    api("org.jetbrains.kotlin:kotlin-stdlib")
}
