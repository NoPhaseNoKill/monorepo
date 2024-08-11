package com.nophasenokill.domain

import org.junit.jupiter.api.Test

class GradleTimeTest {

    @Test
    fun `should be done lazily`() {
        simulateBuildServiceFunction()
    }

    fun simulateBuildServiceFunction(): () -> Long {
        val time = GradleTime.now()
        println("simulateBuildServiceFunction Start time time: ${time}")
        println("simulateBuildServiceFunction Start time time.currentFormatted: ${time.currentFormatted}")
        println("simulateBuildServiceFunction Start time time.current: ${time.current}")
        return { time.current }
    }
}