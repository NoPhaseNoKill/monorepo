package com.nophasenokill.setup.junit.extensions

import com.nophasenokill.setup.runner.SharedRunnerDetails
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

    fun getSharedGradleRunnerDetails(context: ExtensionContext): SharedRunnerDetails {
        return context.root.getStore(GLOBAL_NAMESPACE).get(SharedTestSuiteContextKey.SHARED_GRADLE_RUNNER_DETAILS) as SharedRunnerDetails
    }

    fun getGradleCreationState(context: ExtensionContext): GradleCreationState {
        return context.root.getStore(GLOBAL_NAMESPACE).get(SharedTestSuiteContextKey.GRADLE_CREATION_STATE) as GradleCreationState
    }

    fun getTestGradleDirectory(context: ExtensionContext, uniqueKey: Any): TestDirectory {
        return context.root.getStore(GLOBAL_NAMESPACE).get(uniqueKey) as TestDirectory
    }
}