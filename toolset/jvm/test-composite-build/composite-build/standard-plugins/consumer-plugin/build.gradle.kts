
plugins {
    id("com.nophasenokill.standalone-kotlin-base-plugin") version("1.0.0-local-dev")
    `kotlin-dsl`
    id("com.nophasenokill.producer-plugin") version("1.0.0-local-dev")
}


gradlePlugin {
    plugins {
        create("consumer-plugin") {
            id = "com.nophasenokill.consumer-plugin"
            implementationClass = "com.nophasenokill.ConsumerPlugin"
        }
    }
}

dependencies {

    attributesSchema {
        attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE) {
            compatibilityRules.add(InstrumentedJarsRule::class.java)
        }
    }

    implementation(project(":composite-build:meta-plugins:producer-plugin"))
    implementation(project(":composite-build:base-build-plugins:standalone-kotlin-base-plugin"))

    testImplementation(project(":composite-build:meta-plugins:producer-plugin"))
    // testImplementation("org.junit.jupiter:junit-jupiter")
}

configurations {
    testRuntimeClasspath {
        attributes {
            attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE,
                objects.named(LibraryElements::class.java, "instrumented-jar"))
        }
    }
}

abstract class InstrumentedJarsRule: AttributeCompatibilityRule<LibraryElements> {
    override fun execute(details: CompatibilityCheckDetails<LibraryElements>) = details.run {

        /*
            Means that by default if it can't find instrumented-jar -> it will default back to jar instead

            This is useful in this particular case for external dependencies. They won't have an
            instrumented-jar, and hence will fall back to using the jar variant
         */
        if (consumerValue?.name == "instrumented-jar" && producerValue?.name == "jar") {
            compatible()
        }
    }
}