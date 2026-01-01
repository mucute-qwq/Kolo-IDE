package io.github.mucute.qwq.koloide.module.util

import android.content.Context
import io.github.mucute.qwq.koloide.shared.util.transferToCompat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import java.io.File
import java.io.InputStream
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

suspend fun extractFiles(
    context: Context,
    inputStream: InputStream,
    baseFolder: File
) {
    extractFilesFlow(context, inputStream, baseFolder).collect()
}

@OptIn(ExperimentalUuidApi::class)
fun extractFilesFlow(
    context: Context,
    inputStream: InputStream,
    baseFolder: File
): Flow<ExtractState> {
    val tempFile = File.createTempFile(Uuid.random().toHexString(), null)
    return flow<ExtractState> {

        tempFile.outputStream().use {
            inputStream.transferToCompat(it)
        }

        var fileCount = 0L

        TarArchiveInputStream(GzipCompressorInputStream(tempFile.inputStream())).use { tarArchiveInputStream ->
            while (tarArchiveInputStream.nextEntry != null) {
                fileCount++
            }
        }

        ZipArchiveInputStream(tempFile.inputStream()).use { zipArchiveInputStream ->
            var zipArchiveEntry: ZipArchiveEntry?
            var handledCount = 0f
            while (zipArchiveInputStream.nextEntry.also { nextEntry ->
                    zipArchiveEntry = nextEntry
                } != null) {

                val targetFile = File(baseFolder, zipArchiveEntry!!.name)
                    .normalize()
                    .also { targetFile -> targetFile.delete() }

                emit(
                    ExtractState.Processing(
                        zipArchiveEntry.name,
                        handledCount / fileCount
                    )
                )

                if (zipArchiveEntry.isDirectory) {
                    targetFile.mkdirs()
                    handledCount++
                    continue
                }

                targetFile
                    .outputStream().use { outputStream ->
                        zipArchiveInputStream.transferToCompat(outputStream)
                    }
                targetFile.setExecutable(true)

                handledCount++

            }
        }

    }
}