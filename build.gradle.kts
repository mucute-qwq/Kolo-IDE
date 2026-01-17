@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

plugins {
    id("build-logic.root-project")
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

fun Project.configureApplicationExtension() {
    extensions.findByType<ApplicationExtension>()?.apply {
        configureCommonExtension()

        defaultConfig.apply {
            applicationId = namespace

            targetSdk {
                version = release(Versions.targetSdkVersion)
            }

            versionCode = Versions.versionCode
            versionName = Versions.versionName

            externalNativeBuild {
                cmake {
                    cppFlags += listOf("-std=c++20")
                    arguments += listOf("-DANDROID_STL=c++_shared")
                }
            }
        }

        externalNativeBuild {
            cmake {
                path = file("src/main/cpp/CMakeLists.txt")
                version = "3.22.1"
            }
        }

        signingConfigs {
            create("shared") {
                storeFile = file("../buildKey.jks")
                storePassword = "123456"
                keyAlias = "Kolo-IDE"
                keyPassword = "123456"
                enableV1Signing = true
                enableV2Signing = true
                enableV3Signing = true
            }
        }

        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
                signingConfig = signingConfigs["shared"]
                proguardFiles("proguard-rules.pro")
            }
            getByName("debug") {
                isMinifyEnabled = false
                signingConfig = signingConfigs["shared"]
                proguardFiles("proguard-rules.pro")
            }
        }

        packaging {
            resources {
                excludes += setOf("DebugProbesKt.bin")
            }
        }

        compileOptions {
            isCoreLibraryDesugaringEnabled = true
        }
    }
}

fun Project.configureCommonExtension() {
    extensions.findByType(CommonExtension::class)?.apply {
        compileSdk {
            version = release(Versions.compileSdkVersion)
        }

        buildToolsVersion = Versions.buildToolsVersion

        defaultConfig.apply {
            minSdk {
                version = release(Versions.minSdkVersion)
            }
        }

        buildFeatures.apply {
            compose = true
        }

        compileOptions.apply {
            sourceCompatibility = Versions.javaVersion
            targetCompatibility = Versions.javaVersion
        }
    }
}

fun Project.configureKotlinAndroidExtension() {
    extensions.findByType(KotlinAndroidProjectExtension::class)?.apply {
        compilerOptions {
            freeCompilerArgs.addAll(
                listOf(
                    "-Xcontext-parameters"
                )
            )
        }
        jvmToolchain(Versions.jvmToolchain)
    }
}

subprojects {
    plugins.withId("com.android.application") {
        configureApplicationExtension()
    }
    plugins.withId("com.android.library") {
        configureCommonExtension()
    }
    plugins.withId("org.jetbrains.kotlin.android") {
        configureKotlinAndroidExtension()
    }
}