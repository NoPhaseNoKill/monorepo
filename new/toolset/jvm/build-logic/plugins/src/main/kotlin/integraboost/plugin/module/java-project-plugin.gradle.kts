package integraboost.plugin.module

plugins {
    id("integraboost.plugin.segment.consistent-resolution-plugin")
    id("integraboost.plugin.segment.intellij-settings-plugin")
    id("integraboost.plugin.segment.capability-conflict-avoidance")
}

dependencies {
    implementation(platform("integraboost.platform:base-platform"))
    implementation(platform("integraboost.platform:capability-conflict-avoidance-example-platform"))
}