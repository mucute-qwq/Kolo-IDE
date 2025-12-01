import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import kotlin.plus

plugins {
    id("build-logic.root-project")
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

fun Project.configureBaseExtension() {
    extensions.findByType(BaseExtension::class)?.run {
        compileSdkVersion(Versions.compileSdkVersion)
        buildToolsVersion = Versions.buildToolsVersion

        defaultConfig {
            applicationId = namespace
            minSdk = Versions.minSdkVersion
            targetSdk = Versions.targetSdkVersion
            versionCode = Versions.versionCode
            versionName = Versions.versionName
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

        packagingOptions {
            resources.excludes += setOf("DebugProbesKt.bin")
        }

        buildFeatures.compose = true

        compileOptions {
            sourceCompatibility = Versions.javaVersion
            targetCompatibility = Versions.javaVersion

        }
    }
}

fun Project.configureKotlinExtension() {
    extensions.findByType(KotlinAndroidProjectExtension::class)?.run {
        jvmToolchain(Versions.jvmToolchain)
        compilerOptions {
            freeCompilerArgs.addAll(
                listOf(
                    "-Xcontext-parameters"
                )
            )
        }
    }
}

subprojects {
    plugins.withId("com.android.application") {
        configureBaseExtension()
    }
    plugins.withId("com.android.library") {
        configureBaseExtension()
    }
    plugins.withId("org.jetbrains.kotlin.android") {
        configureKotlinExtension()
    }
}