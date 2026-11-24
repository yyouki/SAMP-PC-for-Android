package com.yyouki.sampdroid.device

/**
 * System thermal states reported by Android's PowerManager API.
 * UNKNOWN is used on older Android versions or when the status is unavailable.
 */
enum class ThermalState(val displayName: String) {
    UNKNOWN("Unavailable"),
    NONE("Normal"),
    LIGHT("Light"),
    MODERATE("Moderate"),
    SEVERE("Severe"),
    CRITICAL("Critical"),
    EMERGENCY("Emergency"),
    SHUTDOWN("Shutdown");

    companion object {
        // Android's public THERMAL_STATUS_* integer values are stable from API 29.
        fun fromPlatformStatus(status: Int): ThermalState = when (status) {
            0 -> NONE
            1 -> LIGHT
            2 -> MODERATE
            3 -> SEVERE
            4 -> CRITICAL
            5 -> EMERGENCY
            6 -> SHUTDOWN
            else -> UNKNOWN
        }
    }
}

/**
 * A point-in-time system and app memory snapshot.
 * Values may be null when the Android service does not expose them.
 */
data class DeviceSnapshot(
    val totalMemoryMb: Long?,
    val availableMemoryMb: Long?,
    val appPssMb: Long?,
    val batteryPercent: Int?,
    val thermalState: ThermalState,
    val powerSaveMode: Boolean?,
    val lowMemory: Boolean?,
    val sampledAtEpochMs: Long,
) {
    val usedMemoryMb: Long?
        get() = if (totalMemoryMb != null && availableMemoryMb != null) {
            (totalMemoryMb - availableMemoryMb).coerceAtLeast(0L)
        } else {
            null
        }

    val usedMemoryPercent: Int?
        get() = calculateUsagePercent(totalMemoryMb, availableMemoryMb)
}

/** Pure helper so memory calculations can be tested without an Android device. */
fun calculateUsagePercent(totalMb: Long?, availableMb: Long?): Int? {
    if (totalMb == null || availableMb == null || totalMb <= 0L) return null

    val used = (totalMb - availableMb).coerceAtLeast(0L)
    return ((used * 100.0) / totalMb)
        .toInt()
        .coerceIn(0, 100)
}

/** Keep dashboard formatting deterministic across device locales. */
fun formatMemoryMb(memoryMb: Long?): String {
    if (memoryMb == null || memoryMb < 0L) return "Unavailable"
    return String.format(java.util.Locale.US, "%.1f GB", memoryMb / 1024.0)
}
