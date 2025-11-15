package io.github.mucute.qwq.koloide.application

import android.app.Application
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

    }

    override fun onTerminate() {
        super.onTerminate()
        appScope.cancel()
    }

}