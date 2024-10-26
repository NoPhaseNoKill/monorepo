import com.nophasenokill.commons.configureJavaToolChain


plugins {
    id("java-library")
    id("groovy-gradle-plugin")
    id("com.nophasenokill.code-quality")
    id("com.nophasenokill.ci-reporting")
    id("com.nophasenokill.test-retry")
    id("com.nophasenokill.private-javadoc")
}

java.configureJavaToolChain()

dependencies {
    api(platform("gradlebuild:build-platform"))
    implementation("com.nophasenokill:gradle-plugin")

    implementation(localGroovy())
    testImplementation("org.spockframework:spock-core")
    testImplementation("net.bytebuddy:byte-buddy")
    testImplementation("org.objenesis:objenesis")
}

tasks.withType<GroovyCompile>().configureEach {
    groovyOptions.apply {
        encoding = "utf-8"
        forkOptions.jvmArgs?.add("-XX:+HeapDumpOnOutOfMemoryError")
    }
    options.apply {
        isFork = true
        encoding = "utf-8"
        compilerArgs = mutableListOf("-Xlint:-options", "-Xlint:-path")
    }
}

tasks.withType<Test>().configureEach {
    val testVersionProvider = javaLauncher.map { it.metadata.languageVersion }
    jvmArgumentProviders.add(CommandLineArgumentProvider {
        //allow ProjectBuilder to inject legacy types into the system classloader
        if (testVersionProvider.get().canCompileOrRun(9)) {
            listOf("--add-opens", "java.base/java.lang=ALL-UNNAMED")
        } else {
            emptyList()
        }
    })
    jvmArgumentProviders.add(CommandLineArgumentProvider {
        val testVersion = testVersionProvider.get()
        if (testVersion.canCompileOrRun(9) && !testVersion.canCompileOrRun(17)) {
            listOf("--illegal-access=deny")
        } else {
            emptyList()
        }
    })
    useJUnitPlatform()
}


