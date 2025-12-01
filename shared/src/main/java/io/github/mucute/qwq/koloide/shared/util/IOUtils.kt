package io.github.mucute.qwq.koloide.shared.util

import java.io.InputStream
import java.io.OutputStream

private const val DEFAULT_BUFFER_SIZE = 8192

fun InputStream.transferToCompat(outputStream: OutputStream, flush: Boolean = true): Long {
    var transferred: Long = 0
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    var read: Int
    while ((read(buffer, 0, DEFAULT_BUFFER_SIZE).also { read = it }) >= 0) {
        outputStream.write(buffer, 0, read)
        if (flush) {
            outputStream.flush()
        }
        transferred += read.toLong()
    }
    return transferred
}