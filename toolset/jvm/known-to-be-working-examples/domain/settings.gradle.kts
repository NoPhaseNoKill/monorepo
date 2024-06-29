
rootProject.name = "domain"

pluginManagement {
    /*
        This implicitly also brings in:
            plugins {
               id("org.jetbrains.kotlin.jvm") version("1.9.22")
            }
     */
    includeBuild("../plugins/root-settings-plugin")
}

plugins {
    id("com.nophasenokill.root-settings-plugin")
}

include("account")
include("person")