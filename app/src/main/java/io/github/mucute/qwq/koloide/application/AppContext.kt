package io.github.mucute.qwq.koloide.application

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import io.github.mucute.qwq.koloide.manager.ExtensionManager
import io.github.mucute.qwq.koloide.receiver.ExtensionBroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

class AppContext : Application() {

    private lateinit var extensionBroadcastReceiver: ExtensionBroadcastReceiver

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

        ExtensionManager.refreshAll()
        registerReceiver(
            ExtensionBroadcastReceiver().also { extensionBroadcastReceiver = it },
            IntentFilter().apply {
                addAction(Intent.ACTION_PACKAGE_ADDED)
                addAction(Intent.ACTION_PACKAGE_REMOVED)
                addAction(Intent.ACTION_PACKAGE_REPLACED)
                addDataScheme("package")
            }
        )
    }

    override fun onTerminate() {
        super.onTerminate()
        appScope.cancel()
        unregisterReceiver(extensionBroadcastReceiver)
    }

}