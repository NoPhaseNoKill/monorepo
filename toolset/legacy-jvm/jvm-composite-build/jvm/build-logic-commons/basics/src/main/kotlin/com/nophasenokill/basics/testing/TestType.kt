

package com.nophasenokill.basics.testing

import org.gradle.api.tasks.testing.Test


enum class TestType(val prefix: String, val executers: List<String>) {
    INTEGRATION("integ", listOf("embedded", "forking", "noDaemon", "parallel", "configCache", "isolatedProjects")),
    CROSSVERSION("crossVersion", listOf("embedded", "forking"))
}


fun Test.includeSpockAnnotation(fqcn: String) {
    systemProperties.compute("include.spock.annotation") { _, oldValue ->
        if (oldValue == null) fqcn else "$oldValue,$fqcn"
    }
}


fun Test.excludeSpockAnnotation(fqcn: String) {
    systemProperties.compute("exclude.spock.annotation") { _, oldValue ->
        if (oldValue == null) fqcn else "$oldValue,$fqcn"
    }
}
