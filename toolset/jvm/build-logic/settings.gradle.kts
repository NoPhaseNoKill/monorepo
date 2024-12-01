import com.nophasenokill.ProjectType

rootProject.name = "build-logic"

pluginManagement {
    includeBuild("plugins")
}

plugins {
    id("com.nophasenokill.build-logic.build-logic-convention-plugins-settings-convention-plugin-one")
    id("com.nophasenokill.build-logic.build-logic-convention-plugins-settings-binary-plugin-one.settings")
}

mySettings {
    applyCustomLogic.set(true)

    includeCustomProject("build-logic-module-one", ProjectType.BUILD_LOGIC, "")
}


