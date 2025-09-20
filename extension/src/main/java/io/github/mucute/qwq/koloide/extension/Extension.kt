package io.github.mucute.qwq.koloide.extension

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo

data class Extension(
    val packageInfo: PackageInfo,
    val applicationInfo: ApplicationInfo,
    val extensionMain: ExtensionMain
)

val Extension.label: String
    get() = applicationInfo.loadLabel(extensionMain.context.packageManager).toString()

val Extension.description: String
    get() = applicationInfo.loadDescription(extensionMain.context.packageManager).toString()