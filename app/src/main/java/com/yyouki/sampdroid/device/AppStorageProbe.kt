package com.yyouki.sampdroid.device

import android.content.Context
import android.os.StatFs

/**
 * Reads free/total space for the filesystem containing this app's private files directory.
 * This does not inspect user-selected external directories or calculate app quotas.
 */
object AppStorageProbe {
    fun readSnapshot(context: Context): StorageSnapshot? {
        return try {
            val stats = StatFs(context.filesDir.absolutePath)
            val blockSize = stats.blockSizeLong
            val totalBytes = blockSize * stats.blockCountLong
            val availableBytes = blockSize * stats.availableBlocksLong

            if (blockSize <= 0L || totalBytes <= 0L || availableBytes < 0L) {
                null
            } else {
                StorageSnapshot(
                    totalBytes = totalBytes,
                    availableBytes = availableBytes.coerceAtMost(totalBytes),
                )
            }
        } catch (_: IllegalArgumentException) {
            null
        } catch (_: SecurityException) {
            null
        }
    }
}
