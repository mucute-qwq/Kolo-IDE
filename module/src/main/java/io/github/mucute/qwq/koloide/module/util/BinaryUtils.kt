package io.github.mucute.qwq.koloide.module.util

import android.content.Context
import android.system.Os
import io.github.mucute.qwq.koloide.shared.util.transferToCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed class ExtractState {

    data class Processing(
        val name: String,
        val progress: Float
    ) : ExtractState()

    object Idle : ExtractState()

}

@OptIn(ExperimentalUuidApi::class)
fun CoroutineScope.extractBinariesFlow(
    context: Context,
    controlName: String,
    inputStream: InputStream
): StateFlow<ExtractState> {
    val tempFile = File.createTempFile(Uuid.random().toHexString(), null)
    return flow<ExtractState> {
        val totalCount = tempFile.outputStream().use {
            inputStream.transferToCompat(it)
        }.toFloat()

        val controlFolder = File(context.filesDir, "control")
        val controlFile = File(controlFolder, "${controlName}.mappings")
        val baseFolder = File(context.filesDir, "usr")
        val gzipCompressorInputStream = GzipCompressorInputStream(tempFile.inputStream())
        controlFile.delete()
        controlFile.createNewFile()

        TarArchiveInputStream(gzipCompressorInputStream).use { tarArchiveInputStream ->
            var tarArchiveEntry: TarArchiveEntry?
            var readCount = 0f
            while (tarArchiveInputStream.nextEntry.also { nextEntry ->
                    tarArchiveEntry = nextEntry
                } != null) {

                val targetFile = File(baseFolder, tarArchiveEntry!!.name)
                    .normalize()
                    .also { targetFile -> targetFile.delete() }

                controlFile.appendText("${targetFile.absolutePath}\n")

                emit(
                    ExtractState.Processing(
                        tarArchiveEntry.name,
                        floor(readCount / totalCount)
                    )
                )

                if (tarArchiveEntry.isLink) {
                    val sourceFile = File(targetFile.parentFile!!, tarArchiveEntry.linkName)
                        .normalize()

                    Os.link(
                        sourceFile.absolutePath,
                        targetFile.absolutePath
                    )
                    continue
                }

                if (tarArchiveEntry.isSymbolicLink) {
                    val sourceFile = File(targetFile.parentFile!!, tarArchiveEntry.linkName)
                        .normalize()

                    Os.symlink(
                        sourceFile.absolutePath,
                        targetFile.absolutePath
                    )
                    continue
                }

                if (tarArchiveEntry.isDirectory) {
                    targetFile.mkdirs()
                    continue
                }

                targetFile
                    .outputStream().use { outputStream ->
                        tarArchiveInputStream.transferToCompat(outputStream)
                    }
                targetFile.setExecutable(true)

                readCount = max(readCount, gzipCompressorInputStream.compressedCount.toFloat())
            }
        }
    }
        .onCompletion {
            tempFile.delete()
            emit(ExtractState.Idle)
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ExtractState.Idle
        )
}