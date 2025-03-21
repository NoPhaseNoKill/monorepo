/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
}

includeBuild("../build-logic-settings")
//
//// Shared basics for all
include("basics")
//
//// Platform: defines shared dependency versions
include("build-platform")
//
//// Compute the identity/version we are building and related details (like current git commit)
include("module-identity")
//
//// Code quality rules common to :build-logic and the root build
//include("code-quality-rules")
//
//// Plugins to build :build-logic plugins
include("gradle-plugin")
//
//// Plugins to publish gradle projects
//include("publishing")

rootProject.name = "build-logic-commons"

// Make sure all the build-logic is compiled for the right Java version
gradle.lifecycle.beforeProject {
    pluginManager.withPlugin("java-base") {
        the<JavaPluginExtension>().toolchain {
            languageVersion = JavaLanguageVersion.of(21)
            vendor = JvmVendorSpec.ADOPTIUM
        }
    }
}
