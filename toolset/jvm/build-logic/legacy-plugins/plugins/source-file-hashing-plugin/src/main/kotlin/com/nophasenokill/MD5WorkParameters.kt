package com.nophasenokill

import org.gradle.api.file.RegularFileProperty
import org.gradle.workers.WorkParameters


interface MD5WorkParameters : WorkParameters {
    val sourceFile: RegularFileProperty
    val mD5File: RegularFileProperty
}

