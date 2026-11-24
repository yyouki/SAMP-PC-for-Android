package com.yyouki.sampdroid.device

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Debug
import android.os.PowerManager
import android.os.Process
import android.os.SystemClock

/**
 * Reads public Android device information.
 *
 * The sampler does not inspect restricted system files, infer GPU models from the SoC,
 * or claim to provide system-wide CPU utilization.
 */
object DeviceProbe {
    fun readCpuTimeSample(): CpuTimeSample = CpuTimeSample(
        processCpuTimeMs = Process.getElapsedCpuTime(),
        elapsedRealtimeMs = SystemClock.elapsedRealtime(),
    )

    fun readDeviceReport(context: Context): DeviceReport {
        val socName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Build.SOC_MODEL.takeIf(String::isNotBlank) ?: Build.HARDWARE
        } else {
            Build.HARDWARE
        }

        return DeviceReport(
            manufacturer = Build.MANUFACTURER.orEmpty(),
            model = Build.MODEL.orEmpty(),
            socModel = socName.orEmpty(),
            hardware = Build.HARDWARE.orEmpty(),
            androidRelease = Build.VERSION.RELEASE.orEmpty(),
            apiLevel = Build.VERSION.SDK_INT,
            cpuThreadCount = Runtime.getRuntime().availableProcessors(),
            totalMemoryMb = readMemoryInfo(context).first,
            supportedAbis = Build.SUPPORTED_ABIS.toList(),
        )
    }

    fun readSnapshot(context: Context): DeviceSnapshot {
        val (totalMemoryMb, availableMemoryMb, lowMemory) = readMemoryInfo(context)

        val processMemoryInfo = Debug.MemoryInfo()
        Debug.getMemoryInfo(processMemoryInfo)
        val appPssMb = processMemoryInfo.totalPss
            .takeIf { it >= 0 }
            ?.toLong()
            ?.div(1024L)

        val batteryManager = context.getSystemService(BatteryManager::class.java)
        val batteryPercent = batteryManager
            ?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            ?.takeIf { it in 0..100 }

        // ACTION_BATTERY_CHANGED is a sticky system broadcast; this reads it without a receiver loop.
        val batteryIntent = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED),
        )
        val batteryStatus = batteryIntent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val pluggedSource = batteryIntent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1

        val powerManager = context.getSystemService(PowerManager::class.java)
        val thermalState = if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && powerManager != null
        ) {
            ThermalState.fromPlatformStatus(powerManager.currentThermalStatus)
        } else {
            ThermalState.UNKNOWN
        }

        return DeviceSnapshot(
            totalMemoryMb = totalMemoryMb,
            availableMemoryMb = availableMemoryMb,
            appPssMb = appPssMb,
            batteryPercent = batteryPercent,
            batteryState = BatteryState.fromPlatformStatus(batteryStatus),
            batterySource = when (pluggedSource) {
                BatteryManager.BATTERY_PLUGGED_AC -> "AC"
                BatteryManager.BATTERY_PLUGGED_USB -> "USB"
                BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Wireless"
                8 -> "Dock"
                0 -> "Battery"
                else -> "Unknown"
            },
            thermalState = thermalState,
            powerSaveMode = powerManager?.isPowerSaveMode,
            lowMemory = lowMemory,
            sampledAtEpochMs = System.currentTimeMillis(),
        )
    }

    private fun readMemoryInfo(context: Context): Triple<Long?, Long?, Boolean?> {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            ?: return Triple(null, null, null)

        val info = ActivityManager.MemoryInfo()
        manager.getMemoryInfo(info)

        val totalMb = info.totalMem
            .takeIf { it > 0L }
            ?.div(1024L * 1024L)
        val availableMb = info.availMem
            .takeIf { it >= 0L }
            ?.div(1024L * 1024L)

        return Triple(totalMb, availableMb, info.lowMemory)
    }
}
