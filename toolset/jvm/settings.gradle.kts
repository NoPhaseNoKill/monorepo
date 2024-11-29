import com.nophasenokill.DefaultProjectType
import com.nophasenokill.mySettings


rootProject.name = "jvm"

pluginManagement {
    includeBuild("build-logic")
}

/*
    Applies to the root settings
 */
plugins {
    id("com.nophasenokill.build-logic.build-logic-convention-plugins-settings-convention-plugin-one")
    id("com.nophasenokill.build-logic.build-logic-convention-plugins-settings-binary-plugin-one.settings")
}

mySettings {
    applyCustomLogic.set(true)

    includeCustomProject("root-one-sub-project-one", DefaultProjectType.RootOne)
    includeCustomProject("root-two-sub-project-one", DefaultProjectType.RootTwo)
    includeCustomProject("build-logic-module-one", DefaultProjectType.BuildLogic)
}
