package com.yyouki.sampdroid.device

import java.time.Instant
import java.util.Locale

/**
 * Builds a plain-text diagnostic summary for explicit user-initiated sharing.
 * The output contains device characteristics and current measurements only.
 */
fun formatDiagnosticReport(
    device: DeviceReport,
    snapshot: DeviceSnapshot,
    appCpuCores: Double?,
    storage: StorageSnapshot? = null,
): String {
    val cpuText = appCpuCores
        ?.takeIf { it.isFinite() && it >= 0.0 }
        ?.let { String.format(Locale.US, "%.2f cores", it) }
        ?: "Unavailable"

    return buildString {
        appendLine("SAMPDROID LITE — DEVICE DIAGNOSTIC REPORT")
        appendLine("Sampled at: ${Instant.ofEpochMilli(snapshot.sampledAtEpochMs)}")
        appendLine()
        appendLine("[DEVICE]")
        appendLine("Name: ${device.displayName.ifBlank { "Unavailable" }}")
        appendLine("Manufacturer: ${device.manufacturer.ifBlank { "Unavailable" }}")
        appendLine("Model: ${device.model.ifBlank { "Unavailable" }}")
        appendLine("SoC: ${device.socModel.ifBlank { "Unavailable" }}")
        appendLine("Hardware: ${device.hardware.ifBlank { "Unavailable" }}")
        appendLine("Android: ${device.androidRelease.ifBlank { "Unavailable" }} (API ${device.apiLevel})")
        appendLine("CPU threads: ${device.cpuThreadCount}")
        appendLine("Supported ABIs: ${device.supportedAbis.joinToString().ifBlank { "Unavailable" }}")
        appendLine()
        appendLine("[MEMORY]")
        appendLine("Total RAM: ${formatMemoryMb(snapshot.totalMemoryMb)}")
        appendLine("Available RAM: ${formatMemoryMb(snapshot.availableMemoryMb)}")
        appendLine("Used RAM: ${formatMemoryMb(snapshot.usedMemoryMb)}")
        appendLine("Used RAM: ${snapshot.usedMemoryPercent?.let { "$it%" } ?: "Unavailable"}")
        appendLine("App PSS: ${formatMemoryMb(snapshot.appPssMb)}")
        appendLine()
        appendLine("[STORAGE]")
        appendLine("Filesystem total: ${formatStorageBytes(storage?.totalBytes)}")
        appendLine("Available space: ${formatStorageBytes(storage?.availableBytes)}")
        appendLine("Used space: ${formatStorageBytes(storage?.usedBytes)}")
        appendLine("Used space: ${storage?.usedPercent?.let { "$it%" } ?: "Unavailable"}")
        appendLine("Note: Storage figures describe the filesystem containing the app's private data, not an app-specific quota.")
        appendLine()
        appendLine("[SYSTEM]")
        appendLine("App CPU: $cpuText")
        appendLine("Battery: ${snapshot.batteryPercent?.let { "$it%" } ?: "Unavailable"}")
        appendLine("Battery state: ${snapshot.batteryState.displayName}")
        appendLine("Power source: ${snapshot.batterySource.ifBlank { "Unavailable" }}")
        appendLine("Thermal status: ${snapshot.thermalState.displayName}")
        appendLine("Power saver: ${snapshot.powerSaveMode?.let { if (it) "On" else "Off" } ?: "Unavailable"}")
        appendLine("Low-memory warning: ${snapshot.lowMemory?.let { if (it) "Active" else "No" } ?: "Unavailable"}")
        appendLine()
        append("Note: App CPU is a process-level core-equivalent estimate, not system-wide CPU utilization.")
    }
}
