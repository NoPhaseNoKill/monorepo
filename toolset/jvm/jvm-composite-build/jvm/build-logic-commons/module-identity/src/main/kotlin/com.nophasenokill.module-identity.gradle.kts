
import  com.nophasenokill.basics.buildFinalRelease
import  com.nophasenokill.basics.buildMilestoneNumber
import  com.nophasenokill.basics.buildRcNumber
import  com.nophasenokill.basics.buildRunningOnCi
import  com.nophasenokill.basics.buildTimestamp
import  com.nophasenokill.basics.buildVersionQualifier
import  com.nophasenokill.basics.ignoreIncomingBuildReceipt
import  com.nophasenokill.basics.isPromotionBuild
import  com.nophasenokill.basics.releasedVersionsFile
import  com.nophasenokill.basics.repoRoot
import com.nophasenokill.identity.extension.ModuleIdentityExtension
import com.nophasenokill.identity.extension.ReleasedVersionsDetails
import com.nophasenokill.identity.provider.BuildTimestampFromBuildReceiptValueSource
import com.nophasenokill.identity.provider.BuildTimestampValueSource
import com.nophasenokill.identity.tasks.BuildReceipt

plugins {
    `java-base`
}

val moduleIdentity = extensions.create<ModuleIdentityExtension>("moduleIdentity")

group = "com.nophasenokill"
version = collectVersionDetails(moduleIdentity)

fun Project.collectVersionDetails(moduleIdentity: ModuleIdentityExtension): String {
    moduleIdentity.baseName.convention("gradle-$name")

    val baseVersion = trimmedContentsOfFile("version.txt")

    val finalRelease = buildFinalRelease
    val rcNumber = buildRcNumber
    val milestoneNumber = buildMilestoneNumber

    if (
        (buildFinalRelease.isPresent && buildRcNumber.isPresent) ||
        (buildFinalRelease.isPresent && buildMilestoneNumber.isPresent) ||
        (buildRcNumber.isPresent && buildMilestoneNumber.isPresent)
    ) {
        throw InvalidUserDataException("Cannot set any combination of milestoneNumber, rcNumber and finalRelease at the same time")
    }

    val versionQualifier = buildVersionQualifier
    val isFinalRelease = finalRelease.isPresent

    val buildTimestamp = buildTimestamp()
    val versionNumber = when {
        isFinalRelease -> {
            baseVersion
        }

        rcNumber.isPresent -> {
            "$baseVersion-rc-${rcNumber.get()}"
        }

        milestoneNumber.isPresent -> {
            "$baseVersion-milestone-${milestoneNumber.get()}"
        }

        versionQualifier.isPresent -> {
            "$baseVersion-${versionQualifier.get()}-${buildTimestamp.get()}"
        }

        else -> {
            "$baseVersion-${buildTimestamp.get()}"
        }
    }

    val isSnapshot = !finalRelease.isPresent && !rcNumber.isPresent && !milestoneNumber.isPresent

    moduleIdentity.version.convention(GradleVersion.version(versionNumber))
    moduleIdentity.snapshot.convention(isSnapshot)
    moduleIdentity.buildTimestamp.convention(buildTimestamp)
    moduleIdentity.promotionBuild.convention(isPromotionBuild)

    moduleIdentity.releasedVersions = provider {
        ReleasedVersionsDetails(
            moduleIdentity.version.get().baseVersion,
            releasedVersionsFile()
        )
    }

    return versionNumber
}

/**
 * Returns the trimmed contents of the file at the given [path] after
 * marking the file as a build logic input.
 */
fun Project.trimmedContentsOfFile(path: String): String =
    providers.fileContents(repoRoot().file(path)).asText.get().trim()

// TODO Simplify the buildTimestamp() calculation if possible
fun Project.buildTimestamp(): Provider<String> =
    providers.of(BuildTimestampValueSource::class) {
        parameters {
            buildTimestampFromBuildReceipt = buildTimestampFromBuildReceipt()
            buildTimestampFromGradleProperty = buildTimestamp
            runningOnCi = buildRunningOnCi
            runningInstallTask = provider { isRunningInstallTask() }
            runningDocsTestTask = provider { isRunningDocsTestTask() }
        }
    }


fun Project.buildTimestampFromBuildReceipt(): Provider<String> =
    providers.of(BuildTimestampFromBuildReceiptValueSource::class) {
        parameters {
            ignoreIncomingBuildReceipt = project.ignoreIncomingBuildReceipt
            buildReceiptFileContents = repoRoot()
                .dir("incoming-distributions")
                .file(BuildReceipt.buildReceiptFileName)
                .let(providers::fileContents)
                .asText
        }
    }


fun isRunningInstallTask() =
    listOf("install", "installAll")
        .flatMap { listOf(":distributions-full:$it", "distributions-full:$it", it) }
        .any(gradle.startParameter.taskNames::contains)

fun isRunningDocsTestTask() =
    setOf(":docs:docsTest", "docs:docsTest")
        .any(gradle.startParameter.taskNames::contains)
