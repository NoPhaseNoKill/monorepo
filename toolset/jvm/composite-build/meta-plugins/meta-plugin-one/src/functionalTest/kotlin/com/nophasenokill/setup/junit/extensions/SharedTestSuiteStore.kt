package com.nophasenokill.setup.junit.extensions

import com.nophasenokill.setup.variations.TestDirectory
import org.junit.jupiter.api.extension.ExtensionContext

object SharedTestSuiteStore {
    private val GLOBAL_NAMESPACE: ExtensionContext.Namespace = ExtensionContext.Namespace.GLOBAL

    fun getRoot(extensionContext: ExtensionContext): ExtensionContext.Store {
        return extensionContext.root.getStore(GLOBAL_NAMESPACE)
    }

    fun putObjectIntoGlobalStore(context: ExtensionContext, uniqueKey: Any, value: Any) {
        context.root.getStore(GLOBAL_NAMESPACE).put(uniqueKey, value)
    }

    fun getTestGradleDirectory(context: ExtensionContext, uniqueKey: Any): TestDirectory {
        return context.root.getStore(GLOBAL_NAMESPACE).get(uniqueKey) as TestDirectory
    }
}