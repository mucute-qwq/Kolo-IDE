@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.github.mucute.qwq.koloide"

    defaultConfig {
        externalNativeBuild {
            cmake {
                cppFlags += ""
            }
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

}

dependencies {
    implementation(project(":shared"))
    implementation(project(":module"))
    implementation(project(":module-deno"))
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}