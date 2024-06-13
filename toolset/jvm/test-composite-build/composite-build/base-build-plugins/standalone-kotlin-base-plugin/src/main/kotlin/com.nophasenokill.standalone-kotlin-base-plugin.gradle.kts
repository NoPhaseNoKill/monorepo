plugins {
    kotlin("jvm")
}

/*
    Configures this for the underlying project that uses the plugin.
    These coordinates allow for us to build into '$rootDir/local-repo',
    which in turn allow us to do the following:

        i) Use plugins at the top level of the composite build of a rootProject
        ii) Create plugins within the composite build of a rootProject
        iii) Have them remain on the test classpath,so that we can
        make them infinitely easier to test
 */
project.apply {
    group = "com.nophasenokill"
    version = "1.0.0-local-dev"
}

project.dependencies {
    implementation(gradleApi())
    testImplementation(gradleTestKit())
}