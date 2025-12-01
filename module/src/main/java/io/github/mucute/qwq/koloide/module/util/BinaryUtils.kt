package io.github.mucute.qwq.koloide.module.util

import android.content.Context
import android.system.Os
import io.github.mucute.qwq.koloide.shared.util.transferToCompat
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import java.io.File
import java.io.InputStream

fun Context.extractBinaries(inputStream: InputStream) {
    val baseFolder = File(filesDir, "usr")
    val gzipCompressorInputStream = GzipCompressorInputStream(inputStream)
    TarArchiveInputStream(gzipCompressorInputStream).use { tarArchiveInputStream ->
        var tarArchiveEntry: TarArchiveEntry?
        while (tarArchiveInputStream.nextEntry.also { tarArchiveEntry = it } != null) {
            val targetFile = File(baseFolder, tarArchiveEntry!!.name)
                .normalize()
                .also { it.delete() }

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
                File(baseFolder, tarArchiveEntry.name).mkdirs()
                continue
            }

            targetFile
                .outputStream().use { outputStream ->
                    tarArchiveInputStream.transferToCompat(outputStream)
                }
            targetFile.setExecutable(true)
        }
    }
}