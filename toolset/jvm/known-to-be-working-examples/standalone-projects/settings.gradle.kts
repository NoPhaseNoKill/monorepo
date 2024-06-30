rootProject.name = "standalone-projects"


plugins {
    id("com.nophasenokill.root-settings-plugin")
}

includeBuild("../plugins/basic-plugin")

include("application-one")
include("library-one")