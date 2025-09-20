package io.github.mucute.qwq.koloide.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import io.github.mucute.qwq.koloide.manager.ExtensionManager

class ExtensionBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        val data = intent.data ?: return
        val isExtraReplacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)
        val packageName = data.schemeSpecificPart
        when {
            action == Intent.ACTION_PACKAGE_ADDED && !isExtraReplacing -> {
                Log.e("ExtensionBroadcastReceiver", "Added package: $packageName")
                ExtensionManager.addExtensionIfNeeded(packageName)
            }

            action == Intent.ACTION_PACKAGE_REMOVED && !isExtraReplacing -> {
                Log.e("ExtensionBroadcastReceiver", "Removed package: $packageName")
                ExtensionManager.removeExtensionIfNeeded(packageName)
            }

            action == Intent.ACTION_PACKAGE_REPLACED -> {
                Log.e("ExtensionBroadcastReceiver", "Replaced package: $packageName")
                ExtensionManager.replaceExtensionIfNeeded(packageName)
            }
        }
    }

}