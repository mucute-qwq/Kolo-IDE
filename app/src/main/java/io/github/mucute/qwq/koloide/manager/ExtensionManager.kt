package io.github.mucute.qwq.koloide.manager

import android.content.Context
import android.content.pm.PackageManager
import dalvik.system.DexClassLoader
import io.github.mucute.qwq.koloide.application.AppContext
import io.github.mucute.qwq.koloide.extension.Extension
import io.github.mucute.qwq.koloide.extension.ExtensionMain
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val appScope = AppContext.Companion.appScope

@Suppress("ConstPropertyName")
object ExtensionManager {

    private const val KoloIDE_Extension_Main = "koloide_extension_main"

    private val _extensions = MutableStateFlow<List<Extension>>(emptyList())

    val extensions = _extensions.asStateFlow()

    fun refresh() {
        appScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }) {
            val context = AppContext.Companion.instance
            val packageManager = context.packageManager
            val packageInfos = packageManager.getInstalledPackages(
                PackageManager.GET_META_DATA
            )
            val packagePairs = packageInfos
                .mapNotNull { packageInfo ->
                    val applicationInfo = packageInfo.applicationInfo ?: return@mapNotNull null
                    val metaData = applicationInfo.metaData ?: return@mapNotNull null
                    metaData.getString(KoloIDE_Extension_Main) ?: return@mapNotNull null
                    packageInfo to applicationInfo
                }
            val extensions = ArrayList<Extension>()

            packagePairs.forEach { packagePair ->
                runCatching {
                    val packageInfo = packagePair.first
                    val applicationInfo = packagePair.second
                    val extensionMainClassName =
                        applicationInfo.metaData.getString(KoloIDE_Extension_Main)!!

                    val resources = packageManager.getResourcesForApplication(applicationInfo)
                    val extensionClassLoader = DexClassLoader(
                        applicationInfo.publicSourceDir,
                        null,
                        applicationInfo.nativeLibraryDir,
                        context.classLoader
                    )

                    val extensionContext = object : android.content.ContextWrapper(
                        context.createPackageContext(
                            packageInfo.packageName,
                            CONTEXT_IGNORE_SECURITY
                        )
                    ) {
                        override fun getClassLoader(): ClassLoader? {
                            return extensionClassLoader
                        }
                    }

                    val extensionMainClass = extensionClassLoader.loadClass(extensionMainClassName)!!
                    val declaredConstructor =
                        extensionMainClass.getDeclaredConstructor(Context::class.java)
                    val extensionMain =
                        declaredConstructor.newInstance(extensionContext)

                    extensions.add(
                        Extension(
                            packageInfo = packageInfo,
                            applicationInfo = applicationInfo,
                            extensionMain = extensionMain as ExtensionMain
                        )
                    )
                }.exceptionOrNull()?.printStackTrace()
            }

            _extensions.update {
                extensions.toList()
            }
        }
    }

}