package com.yyouki.sampdroid.device

import java.util.Locale

/**
 * Storage values for the filesystem containing this app's private files directory.
 * These values describe the containing filesystem, not an app-specific quota.
 */
data class StorageSnapshot(
    val totalBytes: Long,
    val availableBytes: Long,
) {
    val usedBytes: Long
        get() = (totalBytes - availableBytes).coerceAtLeast(0L)

    val usedPercent: Int?
        get() = calculateStorageUsagePercent(totalBytes, availableBytes)
}

/** Calculate used filesystem storage as a bounded percentage. */
fun calculateStorageUsagePercent(totalBytes: Long, availableBytes: Long): Int? {
    if (totalBytes <= 0L || availableBytes < 0L) return null

    val usedBytes = (totalBytes - availableBytes).coerceAtLeast(0L)
    return ((usedBytes.toDouble() / totalBytes.toDouble()) * 100.0)
        .toInt()
        .coerceIn(0, 100)
}

/** Format storage consistently using binary gigabytes and a stable locale. */
fun formatStorageBytes(bytes: Long?): String {
    if (bytes == null || bytes < 0L) return "Unavailable"
    val gib = bytes / (1024.0 * 1024.0 * 1024.0)
    return String.format(Locale.US, "%.2f GB", gib)
}
