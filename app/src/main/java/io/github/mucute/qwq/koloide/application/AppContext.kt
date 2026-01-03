package io.github.mucute.qwq.koloide.application

import android.app.Activity
import android.app.Application
import android.os.Bundle
import io.github.dingyi222666.monarch.languages.JavascriptLanguage
import io.github.mucute.qwq.koloide.manager.ModuleManager
import io.github.mucute.qwq.koloide.manager.ProjectManager
import io.github.mucute.qwq.koloide.module.util.extractBinaries
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

class AppContext : Application(), Application.ActivityLifecycleCallbacks {

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

        filesDir.resolve("control").mkdirs()
        filesDir.resolve("home").mkdirs()
        filesDir.resolve("module").mkdirs()

        appScope.launch(Dispatchers.IO) {
            extractBinaries(this@AppContext, assets.open("bootstrap/merminal-bootstrap.tar"))
        }

        registerActivityLifecycleCallbacks(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        appScope.cancel()
        unregisterActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        ModuleManager.refresh()
    }

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle
    ) {

    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

}