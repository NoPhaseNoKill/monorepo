import com.nophasenokill.ProjectType
import com.nophasenokill.safeGradleExtension


rootProject.name = "jvm"

pluginManagement {

    plugins {
        id("com.gradle.develocity") version "3.18.1"
    }
    includeBuild("build-logic")
}

includeBuild("test-build-logic")
includeBuild("consumer")
includeBuild("producer")

/*
    Applies to the root settings
 */
plugins {
    id("com.gradle.develocity")
    id("com.nophasenokill.build-logic.build-logic-convention-plugins-settings-convention-plugin-one")
    id("com.nophasenokill.build-logic.build-logic-convention-plugins-settings-binary-plugin-one.settings")
}

safeGradleExtension {
    applyCustomLogic.set(true)

    includeCustomProject("root-one-sub-project-one", ProjectType.ROOT_ONE, "root-one")
    includeCustomProject("root-two-sub-project-one", ProjectType.ROOT_TWO, "root-two")
}
