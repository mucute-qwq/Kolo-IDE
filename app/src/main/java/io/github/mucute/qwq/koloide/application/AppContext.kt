package io.github.mucute.qwq.koloide.application

import android.app.Application
import io.github.dingyi222666.monarch.languages.JavascriptLanguage
import io.github.rosemoe.sora.langs.monarch.registry.FileProviderRegistry
import io.github.rosemoe.sora.langs.monarch.registry.MonarchGrammarRegistry
import io.github.rosemoe.sora.langs.monarch.registry.ThemeRegistry
import io.github.rosemoe.sora.langs.monarch.registry.dsl.monarchLanguages
import io.github.rosemoe.sora.langs.monarch.registry.model.ThemeModel
import io.github.rosemoe.sora.langs.monarch.registry.model.ThemeSource
import io.github.rosemoe.sora.langs.monarch.registry.provider.AssetsFileResolver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

class AppContext : Application() {

    companion object {

        lateinit var appScope: CoroutineScope
            private set

        lateinit var instance: AppContext
            private set

    }

    override fun onCreate() {
        super.onCreate()
        appScope = MainScope()
        instance = this

        FileProviderRegistry.addProvider(AssetsFileResolver(assets))
        ThemeRegistry.loadTheme(ThemeModel(ThemeSource("theme/quietlight.json", "quietlight")))
        MonarchGrammarRegistry.INSTANCE.loadGrammars(monarchLanguages {
            language("javascript") {
                monarchLanguage = JavascriptLanguage
                defaultScopeName()
                languageConfiguration = "textmate/javascript/language-configuration.json"
            }
        })
    }

    override fun onTerminate() {
        super.onTerminate()
        appScope.cancel()
    }

}