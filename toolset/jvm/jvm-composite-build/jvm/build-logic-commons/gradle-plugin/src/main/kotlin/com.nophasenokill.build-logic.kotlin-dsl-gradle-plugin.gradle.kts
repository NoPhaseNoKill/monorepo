import com.nophasenokill.commons.configureJavaToolChain
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
    id("org.gradle.kotlin.kotlin-dsl") // this is 'kotlin-dsl' without version
    id("com.nophasenokill.code-quality")
    id("com.nophasenokill.detekt")
    id("com.nophasenokill.ci-reporting")
    id("com.nophasenokill.test-retry")
    id("com.nophasenokill.private-javadoc")
}

java.configureJavaToolChain()

dependencies {
    api(platform("gradlebuild:build-platform"))
    implementation("com.nophasenokill:gradle-plugin")

    testImplementation("org.junit.vintage:junit-vintage-engine")
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        allWarningsAsErrors = true
    }
}

detekt {
    // overwrite the config file's location
    config.convention(project.isolated.rootProject.projectDirectory.file("../gradle/detekt.yml"))
}

tasks.named("codeQuality") {
    dependsOn("detekt")
}

tasks.validatePlugins {
    failOnWarning = true
    enableStricterValidation = true
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
