plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.githun.mucute.qwq.koloide"

    defaultConfig {
        applicationId = "io.githun.mucute.qwq.koloide"
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
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs["shared"]
            proguardFiles("proguard-rules.pro")
        }
        debug {
            isMinifyEnabled = false
            signingConfig = signingConfigs["shared"]
            proguardFiles("proguard-rules.pro")
        }
    }

    packaging {
        resources.excludes += setOf("DebugProbesKt.bin")
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":shared"))
}