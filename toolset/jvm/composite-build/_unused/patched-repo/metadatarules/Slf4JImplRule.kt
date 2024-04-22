package com.nophasenokill.metadatarules

import org.gradle.api.artifacts.CacheableRule
import org.gradle.api.artifacts.ComponentMetadataContext
import org.gradle.api.artifacts.ComponentMetadataRule

@CacheableRule
abstract class Slf4JImplRule : ComponentMetadataRule {

    override fun execute(context: ComponentMetadataContext) {
        val version = context.details.id.version
        Logging.getLogger("SharedAppExtension").lifecycle("Belongs to: ${context.details.id.name}:${context.details.id.group}:${context.details.id.version}")
        context.details.allVariants {
            withCapabilities {
                addCapability("org.slf4j", "slf4j-impl", version)
            }
        }
    }
}