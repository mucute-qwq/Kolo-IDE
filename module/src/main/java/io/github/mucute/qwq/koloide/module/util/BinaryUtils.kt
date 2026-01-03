package io.github.mucute.qwq.koloide.module.util

import android.content.Context
import android.system.Os
import io.github.mucute.qwq.koloide.shared.util.transferToCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import java.io.File
import java.io.InputStream
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

suspend fun extractBinaries(
    context: Context,
    inputStream: InputStream
) {
    extractBinariesFlow(context, inputStream).collect()
}

@OptIn(ExperimentalUuidApi::class)
fun extractBinariesFlow(
    context: Context,
    inputStream: InputStream
): Flow<ExtractState> {
    val tempFile = File.createTempFile(Uuid.random().toHexString(), null)
    return flow<ExtractState> {

        val moduleFolder = File(context.filesDir, "module")
        var controlName = ""

        TarArchiveInputStream(inputStream).use { tarArchiveInputStream ->
            var tarArchiveEntry: TarArchiveEntry?
            while (tarArchiveInputStream.nextEntry.also { nextEntry ->
                    tarArchiveEntry = nextEntry
                } != null) {

                emit(
                    ExtractState.Processing(
                        tarArchiveEntry!!.name,
                        0f
                    )
                )

                if (tarArchiveEntry.name.endsWith("module-info.json")) {
                    File(moduleFolder, tarArchiveEntry.name)
                        .normalize()
                        .outputStream().use {
                            tarArchiveInputStream.transferToCompat(it)
                        }

                    controlName = Json.parseToJsonElement(
                        File(moduleFolder, tarArchiveEntry.name)
                            .normalize()
                            .readText()
                    ).jsonObject["type"]!!.jsonPrimitive.content
                    continue
                }

                tempFile.outputStream().use {
                    tarArchiveInputStream.transferToCompat(it)
                }

            }
        }

        var fileCount = 0L

        TarArchiveInputStream(GzipCompressorInputStream(tempFile.inputStream())).use { tarArchiveInputStream ->
            while (tarArchiveInputStream.nextEntry != null) {
                fileCount++
            }
        }

        val controlFolder = File(context.filesDir, "control")
        val controlFile = File(controlFolder, "${controlName}.mappings")
        val baseFolder = File(context.filesDir, "usr")
        val gzipCompressorInputStream = GzipCompressorInputStream(tempFile.inputStream())
        controlFile.delete()
        controlFile.createNewFile()

        TarArchiveInputStream(gzipCompressorInputStream).use { tarArchiveInputStream ->
            var tarArchiveEntry: TarArchiveEntry?
            var handledCount = 0f
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
                        handledCount / fileCount
                    )
                )

                if (tarArchiveEntry.isLink) {
                    val sourceFile = File(targetFile.parentFile!!, tarArchiveEntry.linkName)
                        .normalize()

                    Os.link(
                        sourceFile.absolutePath,
                        targetFile.absolutePath
                    )

                    handledCount++
                    continue
                }

                if (tarArchiveEntry.isSymbolicLink) {
                    val sourceFile = File(targetFile.parentFile!!, tarArchiveEntry.linkName)
                        .normalize()

                    Os.symlink(
                        sourceFile.absolutePath,
                        targetFile.absolutePath
                    )

                    handledCount++
                    continue
                }

                if (tarArchiveEntry.isDirectory) {
                    targetFile.mkdirs()
                    handledCount++
                    continue
                }

                targetFile
                    .outputStream().use { outputStream ->
                        tarArchiveInputStream.transferToCompat(outputStream)
                    }
                targetFile.setExecutable(true)

                handledCount++
            }
        }
    }.onCompletion {
        tempFile.delete()
        emit(ExtractState.Idle)
    }.flowOn(Dispatchers.IO)
}