package com.nophasenokill
//
// import org.gradle.api.Plugin
// import org.gradle.api.Project
// import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry
// import org.jetbrains.kotlin.gradle.plugin.*
// import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformPluginBase.Companion.applyPlugin
// import org.jetbrains.kotlin.gradle.plugin.internal.*
// import org.jetbrains.kotlin.gradle.targets.js.nodejs.UnameExecutor
// import javax.inject.Inject
//
// class VariantPlugin {
//
//     open class KotlinPluginWrapper @Inject constructor(
//         registry: ToolingModelBuilderRegistry
//     ) : AbstractKotlinPluginWrapper(registry) {
//
//         override val pluginVariant: String = PLUGIN_VARIANT_NAME
//
//         override fun apply(project: Project) {
//             project.registerVariantImplementations()
//             super.apply(project)
//         }
//     }
//
//     open class KotlinPlatformJvmPlugin : KotlinPlatformImplementationPluginBase("jvm") {
//         override fun apply(project: Project) {
//             project.applyPlugin<KotlinPluginWrapper>()
//             super.apply(project)
//         }
//     }
//
//     open class KotlinPlatformImplementationPluginBase(platformName: String) : KotlinPlatformPluginBase(platformName) {
//         override fun apply(target: Project) {
//             TODO("Not yet implemented")
//         }
//
//     }
//
//     abstract class KotlinPlatformPluginBase(protected val platformName: String) : Plugin<Project> {
//         companion object {
//             @JvmStatic
//             protected inline fun <reified T : Plugin<*>> Project.applyPlugin() {
//                 pluginManager.apply(T::class.java)
//             }
//         }
//     }
//
//     // private fun Project.registerVariantImplementations() {
//     //     val factories = VariantImplementationFactoriesConfigurator.get(gradle)
//     //     factories[MavenPluginConfigurator.MavenPluginConfiguratorVariantFactory::class] =
//     //         MavenPluginConfiguratorG6.Gradle6MavenPluginConfiguratorVariantFactory()
//     //     factories[JavaSourceSetsAccessor.JavaSourceSetsAccessorVariantFactory::class] =
//     //         JavaSourceSetsAccessorG6.JavaSourceSetAccessorVariantFactoryG6()
//     //     factories[BasePluginConfiguration.BasePluginConfigurationVariantFactory::class] =
//     //         BasePluginConfigurationG6.BasePluginConfigurationVariantFactoryG6()
//     //     factories[IdeaSyncDetector.IdeaSyncDetectorVariantFactory::class] =
//     //         IdeaSyncDetectorG6.IdeaSyncDetectorVariantFactoryG6()
//     //     factories[ConfigurationTimePropertiesAccessor.ConfigurationTimePropertiesAccessorVariantFactory::class] =
//     //         ConfigurationTimePropertiesAccessorG6.ConfigurationTimePropertiesAccessorVariantFactoryG6()
//     //     factories[MppTestReportHelper.MppTestReportHelperVariantFactory::class] =
//     //         MppTestReportHelperG6.MppTestReportHelperVariantFactoryG6()
//     //     factories[KotlinTestReportCompatibilityHelper.KotlinTestReportCompatibilityHelperVariantFactory::class] =
//     //         KotlinTestReportCompatibilityHelperG6.KotlinTestReportCompatibilityHelperVariantFactoryG6()
//     //     factories[ArtifactTypeAttributeAccessor.ArtifactTypeAttributeAccessorVariantFactory::class] =
//     //         ArtifactTypeAttributeAccessorG6.ArtifactTypeAttributeAccessorVariantFactoryG6()
//     //     factories[ProjectIsolationStartParameterAccessor.Factory::class] =
//     //         ProjectIsolationStartParameterAccessorG6.Factory()
//     //     factories[CompatibilityConventionRegistrar.Factory::class] =
//     //         CompatibilityConventionRegistrarG6.Factory()
//     //     factories[UnameExecutor.UnameExecutorVariantFactory::class] =
//     //         UnameExecutorG6.UnameExecutorVariantFactoryG6()
//     //     factories[ConfigurationCacheStartParameterAccessor.Factory::class] = ConfigurationCacheStartParameterAccessorG6.Factory()
//     // }
// }