plugins {
    id("base")
    id("test-report-aggregation")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    testReportAggregation(platform("com.nophasenokill.platforms:generalised-platform"))
    testReportAggregation("com.nophasenokill.standalone-plugins:standalone-plugin-one")
    testReportAggregation("com.nophasenokill.meta-plugins:meta-plugin-one")
    testReportAggregation("com.nophasenokill.applications:application-one")
    testReportAggregation("com.nophasenokill.platforms:generalised-platform")
}

reporting {
    reports {
        val testAggregateTestReport by creating(AggregateTestReport::class) {
            testType = TestSuiteType.UNIT_TEST
        }

        val testAggregateFunctionalTestReport by creating(AggregateTestReport::class) {
            testType = TestSuiteType.FUNCTIONAL_TEST
        }
    }
}

tasks.check {
    dependsOn(tasks.named<TestReport>("testAggregateTestReport"))
    dependsOn(tasks.named<TestReport>("testAggregateFunctionalTestReport"))
}