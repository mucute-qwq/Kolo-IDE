plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.github.mucute.qwq.koloide.shared"
}

dependencies {
    api(libs.commons.compress)
    api(platform(libs.editor.bom))
    api(libs.editor)
    api(libs.editor.lsp)
    api(libs.oniguruma.native)
    api(libs.language.monarch)
    api(libs.monarch.code)
    api(libs.monarch.language.pack)
    api(libs.lsp4j)
    api(libs.compose.preference)
    api(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.activity.compose)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.material3)
    api(libs.androidx.navigation.compose)
    api(libs.androidx.material.icons.extended)
    api(libs.kotlinx.serialization.json)
    debugApi(libs.androidx.ui.tooling)
    debugApi(libs.androidx.ui.test.manifest)
}