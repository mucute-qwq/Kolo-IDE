package io.github.mucute.qwq.koloide.application

import android.app.Application
import io.githun.mucute.qwq.koloide.manager.ExtensionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

class AppContext : android.app.Application() {

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

        _root_ide_package_.io.github.mucute.qwq.koloide.manager.ExtensionManager.refresh()
    }

    override fun onTerminate() {
        super.onTerminate()
        appScope.cancel()
    }

}