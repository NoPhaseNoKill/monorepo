import org.gradle.api.internal.FeaturePreviews
import java.io.PrintWriter
import java.io.Serializable

pluginManagement {
    repositories {
        maven {
            url = uri("https://repo.gradle.org/gradle/enterprise-libs-release-candidates")
            content {
                val rcAndMilestonesPattern = "\\d{1,2}?\\.\\d{1,2}?(\\.\\d{1,2}?)?-((rc-\\d{1,2}?)|(milestone-\\d{1,2}?))"
                // GE plugin marker artifact
                includeVersionByRegex("com.gradle.develocity", "com.gradle.develocity.gradle.plugin", rcAndMilestonesPattern)
                // GE plugin jar
                includeVersionByRegex("com.gradle", "develocity-gradle-plugin", rcAndMilestonesPattern)
            }
        }
        maven {
            name = "Gradle public repository"
            url = uri("https://repo.gradle.org/gradle/public")
            content {
                includeModule("org.openmbee.junit", "junit-xml-parser")
            }
        }
        gradlePluginPortal()
    }
    includeBuild("build-logic-settings")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version ("0.8.0")
    id("com.nophasenokill.build-environment")
    id("com.nophasenokill.develocity-config")

}

includeBuild("build-logic-commons")
includeBuild("build-logic")

apply(from = "gradle/shared-with-buildSrc/mirrors.settings.gradle.kts")

rootProject.name = "gradle"

FeaturePreviews.Feature.values().forEach { feature ->
    if (feature.isActive) {
        enableFeaturePreview(feature.name)
    }
}

fun remoteBuildCacheEnabled(settings: Settings) = settings.buildCache.remote?.isEnabled == true

fun getBuildJavaHome() = System.getProperty("java.home")

gradle.settingsEvaluated {
    if ("true" == System.getProperty("org.gradle.ignoreBuildJavaVersionCheck")) {
        return@settingsEvaluated
    }

    if (JavaVersion.current() != JavaVersion.VERSION_17) {
        throw GradleException("This build requires JDK 17. It's currently ${getBuildJavaHome()}. You can ignore this check by passing '-Dorg.gradle.ignoreBuildJavaVersionCheck=true'.")
    }
}

fun platform(platformName: String, platformConfiguration: PlatformBuilder.() -> Unit): PlatformBuilder {
    val platform = PlatformBuilder(platformName)
    platform.platformConfiguration()
    return platform
}

class ProjectScope(
    private val basePath: String
) {
    fun subproject(projectName: String) {
        include(projectName)
        project(":$projectName").projectDir = file("$basePath/$projectName")
    }
}

class ElementId(val id: String) : Serializable {
    override fun toString(): String {
        return id
    }
}

sealed class ArchitectureElement(
    val name: String,
    val id: ElementId
) : java.io.Serializable

class Platform(name: String, id: ElementId, val uses: List<ElementId>, val children: List<ArchitectureModule>) : ArchitectureElement(name, id)

class ArchitectureModule(name: String, id: ElementId) : ArchitectureElement(name, id)

sealed class ArchitectureElementBuilder(
    val name: String
) {
    val id: ElementId = ElementId(name.replace("-", "_"))

    abstract fun build(): ArchitectureElement
}

class ArchitectureModuleBuilder(
    name: String,
    private val projectScope: ProjectScope
) : ArchitectureElementBuilder(name) {
    constructor(name: String) : this(name, ProjectScope("platforms/$name"))

    fun subproject(projectName: String) {
        projectScope.subproject(projectName)
    }

    override fun build(): ArchitectureModule {
        return ArchitectureModule(name, id)
    }
}

class PlatformBuilder(
    name: String,
    private val projectScope: ProjectScope
) : ArchitectureElementBuilder(name) {
    private val modules = mutableListOf<ArchitectureModuleBuilder>()
    private val uses = mutableListOf<PlatformBuilder>()

    constructor(name: String) : this(name, ProjectScope("platforms/$name"))

    fun subproject(projectName: String) {
        projectScope.subproject(projectName)
    }

    fun uses(platform: PlatformBuilder) {
        uses.add(platform)
    }

    fun module(platformName: String, moduleConfiguration: ArchitectureModuleBuilder.() -> Unit) {
        val module = ArchitectureModuleBuilder(platformName)
        modules.add(module)
        module.moduleConfiguration()
    }

    override fun build(): Platform {
        return Platform(name, id, uses.map { it.id }, modules.map { it.build() })
    }
}

// endregion
