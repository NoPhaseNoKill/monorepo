plugins {
    id("base")
    id("test-report-aggregation")
}

dependencies {
    testReportAggregation("com.nophasenokill.applications:application-one")
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