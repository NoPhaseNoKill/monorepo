package com.nophasenokill.identity.provider

import com.nophasenokill.identity.tasks.BuildReceipt
import org.gradle.api.Describable
import org.gradle.api.provider.Property
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.api.tasks.Optional


abstract class BuildTimestampFromBuildReceiptValueSource : ValueSource<String, BuildTimestampFromBuildReceiptValueSource.Parameters>, Describable {

    interface Parameters : ValueSourceParameters {

        val ignoreIncomingBuildReceipt: Property<Boolean>

        @get:Optional
        val buildReceiptFileContents: Property<String>
    }

    override fun obtain(): String? = parameters.run {
        buildReceiptString()
            ?.let(BuildReceipt::readBuildReceiptFromString)
            ?.let { buildReceipt ->
                buildReceipt["buildTimestamp"] as String
            }
    }

    override fun getDisplayName(): String =
        "the build timestamp extracted from the build receipt".let {
            when {
                parameters.ignoreIncomingBuildReceipt.get() -> "$it (ignored)"
                else -> it
            }
        }

    private
    fun Parameters.buildReceiptString(): String? = when {
        ignoreIncomingBuildReceipt.get() -> null
        else -> buildReceiptFileContents.orNull
    }
}
