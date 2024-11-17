package com.nophasenokill.tasks

import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.CacheableTask

@CacheableTask
abstract class IncrementalTestTask : Test() {
    //TODO Add incremental support
}

