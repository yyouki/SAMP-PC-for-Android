package com.yyouki.sampdroid.device

import android.app.ActivityManager
import android.content.Context
import android.os.Build

/**
 * Read-only device facts used to select tested runtime profiles later.
 * Unavailable platform data is represented as null or an explicit fallback.
 */
data class DeviceReport(
    val manufacturer: String,
    val model: String,
    val socModel: String,
    val hardware: String,
    val androidRelease: String,
    val apiLevel: Int,
    val cpuThreadCount: Int,
    val totalMemoryMb: Long?,
    val supportedAbis: List<String>,
) {
    val displayName: String
        get() = listOf(manufacturer, model)
            .filter(String::isNotBlank)
            .distinct()
            .joinToString(" ")
}

object DeviceProbe {
    fun read(context: Context): DeviceReport {
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager

        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager?.getMemoryInfo(memoryInfo)

        val socName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Build.SOC_MODEL.takeIf(String::isNotBlank) ?: Build.HARDWARE
        } else {
            Build.HARDWARE
        }

        val totalMemoryMb = memoryInfo.totalMem
            .takeIf { it > 0L }
            ?.div(1024L * 1024L)

        return DeviceReport(
            manufacturer = Build.MANUFACTURER.orEmpty(),
            model = Build.MODEL.orEmpty(),
            socModel = socName.orEmpty(),
            hardware = Build.HARDWARE.orEmpty(),
            androidRelease = Build.VERSION.RELEASE.orEmpty(),
            apiLevel = Build.VERSION.SDK_INT,
            cpuThreadCount = Runtime.getRuntime().availableProcessors(),
            totalMemoryMb = totalMemoryMb,
            supportedAbis = Build.SUPPORTED_ABIS.toList(),
        )
    }
}
