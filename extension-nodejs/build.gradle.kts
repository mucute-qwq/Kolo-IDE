plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.github.mucute.qwq.koloide.extension.nodejs"
}

dependencies {
    compileOnly(project(":extension"))
    compileOnly(project(":shared"))
}