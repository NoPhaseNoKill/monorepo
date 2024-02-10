
group = "com.nophasenokill.jvm"

configurations.all {
    resolutionStrategy {
        failOnVersionConflict()
        failOnDynamicVersions()
        failOnNonReproducibleResolution()
        failOnChangingVersions()
    }
}