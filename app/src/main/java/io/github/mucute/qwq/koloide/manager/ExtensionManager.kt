package io.github.mucute.qwq.koloide.manager

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import dalvik.system.BaseDexClassLoader
import dalvik.system.DexClassLoader
import io.github.mucute.qwq.koloide.application.AppContext
import io.github.mucute.qwq.koloide.extension.Extension
import io.github.mucute.qwq.koloide.extension.ExtensionMain
import io.github.mucute.qwq.koloide.extension.packageName
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val appScope = AppContext.Companion.appScope

@Suppress("ConstPropertyName")
object ExtensionManager {

    private const val KoloIDE_Extension_Main = "koloide_extension_main"

    private val _extensions = MutableStateFlow<List<Extension>>(emptyList())

    val extensions = _extensions.asStateFlow()

    fun refreshAll() {
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
                    val extensionMain =
                        createExtensionMain(extensionMainClassName, applicationInfo, packageInfo)

                    extensions.add(
                        Extension(
                            packageInfo = packageInfo,
                            applicationInfo = applicationInfo,
                            extensionMain = extensionMain
                        )
                    )
                }.exceptionOrNull()?.printStackTrace()
            }

            _extensions.update {
                extensions
                    .sortedBy { it.packageName }
                    .toList()
            }
        }
    }

    fun addExtensionIfNeeded(packageName: String) {
        appScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }) {
            addExtensionIfNeededSuspend(packageName)
        }
    }

    private suspend fun addExtensionIfNeededSuspend(packageName: String) {
        return withContext(Dispatchers.IO) {
            val context = AppContext.Companion.instance
            val packageManager = context.packageManager
            val packageInfo =
                packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
                    ?: return@withContext
            val applicationInfo = packageInfo.applicationInfo ?: return@withContext
            val metaData = applicationInfo.metaData ?: return@withContext
            val extensionMainClassName = metaData.getString(KoloIDE_Extension_Main) ?: return@withContext
            val extensionMain =
                createExtensionMain(extensionMainClassName, applicationInfo, packageInfo)

            val extension = Extension(
                packageInfo = packageInfo,
                applicationInfo = applicationInfo,
                extensionMain = extensionMain
            )

            _extensions.update { extensions ->
                (extensions + listOf(extension))
                    .sortedBy { it.packageName }
            }
        }
    }

    fun removeExtensionIfNeeded(packageName: String) {
        appScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }) {
            removeExtensionIfNeededSuspend(packageName)
        }
    }

    private suspend fun removeExtensionIfNeededSuspend(packageName: String) {
        return withContext(Dispatchers.IO) {
            val extension = extensions.value.find { it.packageName == packageName } ?: return@withContext
            _extensions.update { extensions ->
                extensions.toMutableList()
                    .apply { remove(extension) }
                    .sortedBy { it.packageName }
                    .toList()
            }
        }
    }

    fun replaceExtensionIfNeeded(packageName: String) {
        appScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }) {
            replaceExtensionIfNeededSuspend(packageName)
        }
    }

    private suspend fun replaceExtensionIfNeededSuspend(packageName: String) {
        return withContext(Dispatchers.IO) {
            removeExtensionIfNeededSuspend(packageName)
            addExtensionIfNeededSuspend(packageName)
        }
    }

    private fun createClassLoader(
        applicationInfo: ApplicationInfo
    ): BaseDexClassLoader {
        return DexClassLoader(
            applicationInfo.publicSourceDir,
            null,
            applicationInfo.nativeLibraryDir,
            AppContext.instance.classLoader
        )
    }

    private fun createContext(
        packageInfo: PackageInfo,
        classLoader: ClassLoader
    ): ContextWrapper {
        return object : ContextWrapper(
            AppContext.instance.createPackageContext(
                packageInfo.packageName,
                CONTEXT_IGNORE_SECURITY
            )
        ) {
            override fun getClassLoader(): ClassLoader? {
                return classLoader
            }
        }
    }

    private fun createExtensionMain(
        extensionMainClassName: String,
        extensionClassLoader: ClassLoader,
        extensionContext: Context
    ): ExtensionMain {
        val extensionMainClass =
            extensionClassLoader.loadClass(extensionMainClassName)!!
        val declaredConstructor =
            extensionMainClass.getDeclaredConstructor(Context::class.java)
        return declaredConstructor.newInstance(extensionContext) as ExtensionMain
    }

    private fun createExtensionMain(
        extensionMainClassName: String,
        applicationInfo: ApplicationInfo,
        packageInfo: PackageInfo
    ): ExtensionMain {
        val extensionClassLoader = createClassLoader(applicationInfo)
        return createExtensionMain(
            extensionMainClassName,
            extensionClassLoader,
            createContext(packageInfo, extensionClassLoader)
        )
    }

}