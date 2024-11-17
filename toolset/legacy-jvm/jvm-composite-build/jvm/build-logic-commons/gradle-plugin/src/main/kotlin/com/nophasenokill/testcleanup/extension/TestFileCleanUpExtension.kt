
package com.nophasenokill.testcleanup.extension

import org.gradle.api.provider.Property


/**
 *  An extension for project build script to configure whether the leftover files should be treated:
 *  report only, or a failure.
 */
interface TestFileCleanUpExtension {
    val reportOnly: Property<Boolean>
}
