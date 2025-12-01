package io.github.mucute.qwq.koloide.application

import android.app.Application
import android.util.Log
import io.github.dingyi222666.monarch.languages.JavascriptLanguage
import io.github.mucute.qwq.koloide.module.util.extractBinariesFlow
import io.github.rosemoe.sora.langs.monarch.registry.FileProviderRegistry
import io.github.rosemoe.sora.langs.monarch.registry.MonarchGrammarRegistry
import io.github.rosemoe.sora.langs.monarch.registry.ThemeRegistry
import io.github.rosemoe.sora.langs.monarch.registry.dsl.monarchLanguages
import io.github.rosemoe.sora.langs.monarch.registry.model.ThemeModel
import io.github.rosemoe.sora.langs.monarch.registry.model.ThemeSource
import io.github.rosemoe.sora.langs.monarch.registry.provider.AssetsFileResolver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
                scopeName = "source.js"
                languageConfiguration = "textmate/javascript/language-configuration.json"
            }
        })

        filesDir.resolve("home").mkdirs()
        appScope.launch(Dispatchers.IO) {
            extractBinariesFlow(this@AppContext, assets.open("bootstrap/merminal-bootstrap.tgz"))
                .collect()
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        appScope.cancel()
    }

}