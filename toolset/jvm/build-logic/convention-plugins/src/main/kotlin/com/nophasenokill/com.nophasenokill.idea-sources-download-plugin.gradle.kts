import com.nophasenokill.extensions.configurePlugin

gradle.beforeSettings {
    pluginManager.withPlugin("java") {
        plugins.apply("idea")

        configurePlugin<IdeaPlugin> {
            model.module {
                isDownloadSources = true
            }
        }
    }
}
