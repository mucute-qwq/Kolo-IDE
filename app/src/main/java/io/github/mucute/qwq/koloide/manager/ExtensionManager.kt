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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

private val appScope = AppContext.Companion.appScope

@Suppress("ConstPropertyName")
object ExtensionManager {

    private const val KoloIDE_Extension_Main = "koloide_extension_main"

    private val _state = MutableStateFlow(State.Processing)

    val state = _state.asStateFlow()

    private val _extensions = MutableStateFlow<List<Extension>>(emptyList())

    val extensions = _extensions.asStateFlow()

    fun refreshAll() {
        appScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->

        }) {
            _state.update { State.Processing }
            _extensions.update { refreshAllSuspend() }

        }.invokeOnCompletion {
            _state.update { State.Idle }
        }
    }

    private suspend fun refreshAllSuspend(): List<Extension> {
        return withContext(Dispatchers.IO) {
            val context = AppContext.Companion.instance
            val packageManager = context.packageManager
            val packageNames = packageManager.getInstalledPackages(0).map { it.packageName }
            val extensions = ArrayList<Extension>()

            supervisorScope {
                packageNames.forEach { packageName ->
                    launch {
                        extensions += createExtensionSuspend(packageName)
                    }
                }
            }

            extensions
                .sortedBy { it.packageName }
                .toList()
        }
    }

    private suspend fun findExtensionSuspend(packageName: String): Extension? {
        return withContext(Dispatchers.IO) {
            _extensions.value.find { it.packageName == packageName }
        }
    }

    private suspend fun createExtensionSuspend(packageName: String): Extension {
        return withContext(Dispatchers.IO) {
            val context = AppContext.Companion.instance
            val packageManager = context.packageManager
            val packageInfo =
                packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)!!
            val applicationInfo =
                packageInfo.applicationInfo!!
            val metaData = applicationInfo.metaData!!
            val extensionMainClassName =
                metaData.getString(KoloIDE_Extension_Main)!!
            val extensionMain =
                createExtensionMain(extensionMainClassName, applicationInfo, packageInfo)

            Extension(
                packageInfo = packageInfo,
                applicationInfo = applicationInfo,
                extensionMain = extensionMain
            )
        }
    }

    fun addExtensionIfNeeded(packageName: String) {
        appScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }) {
            _state.update { State.Processing }
            delay(0.5.seconds)
            _extensions.update { addExtensionIfNeededSuspend(packageName) }

        }.invokeOnCompletion {
            _state.update { State.Idle }
        }
    }

    private suspend fun addExtensionIfNeededSuspend(packageName: String): List<Extension> {
        return withContext(Dispatchers.IO) {
            val extensions = _extensions.value
            if (findExtensionSuspend(packageName) != null) {
                return@withContext extensions
            }

            (extensions + createExtensionSuspend(packageName))
                .sortedBy { it.packageName }
                .toList()
        }
    }

    fun removeExtensionIfNeeded(packageName: String) {
        appScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }) {
            _state.update { State.Processing }
            delay(0.5.seconds)
            _extensions.update { removeExtensionIfNeededSuspend(packageName) }

        }.invokeOnCompletion {
            _state.update { State.Idle }
        }
    }

    private suspend fun removeExtensionIfNeededSuspend(packageName: String): List<Extension> {
        return withContext(Dispatchers.IO) {
            val extensions = _extensions.value
            val extension = findExtensionSuspend(packageName) ?: return@withContext extensions

            (extensions - extension)
                .sortedBy { it.packageName }
                .toList()
        }
    }

    fun replaceExtensionIfNeeded(packageName: String) {
        appScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }) {
            _state.update { State.Processing }
            delay(0.5.seconds)
            _extensions.update { replaceExtensionIfNeededSuspend(packageName) }

        }.invokeOnCompletion {
            _state.update { State.Idle }
        }
    }

    private suspend fun replaceExtensionIfNeededSuspend(packageName: String): List<Extension> {
        return withContext(Dispatchers.IO) {
            val extensions = _extensions.value
            val extension = findExtensionSuspend(packageName)?.let { listOf(it) } ?: emptyList()
            val newExtension = try {
                listOf(createExtensionSuspend(packageName))
            } catch (e: Throwable) {
                if (e is CancellationException) throw e
                emptyList()
            }

            (extensions - extension + newExtension)
                .sortedBy { it.packageName }
                .toList()
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

    enum class State {


        Idle, Processing

    }

}