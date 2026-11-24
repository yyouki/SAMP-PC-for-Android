package com.yyouki.sampdroid.device

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DiagnosticReportFormatterTest {
    @Test
    fun includesDeviceAndCurrentTelemetry() {
        val device = DeviceReport(
            manufacturer = "Example",
            model = "Model X",
            socModel = "SoC Y",
            hardware = "board-z",
            androidRelease = "16",
            apiLevel = 36,
            cpuThreadCount = 8,
            totalMemoryMb = 8_192,
            supportedAbis = listOf("arm64-v8a"),
        )
        val snapshot = DeviceSnapshot(
            totalMemoryMb = 8_192,
            availableMemoryMb = 2_048,
            appPssMb = 256,
            batteryPercent = 75,
            batteryState = BatteryState.CHARGING,
            batterySource = "USB",
            thermalState = ThermalState.MODERATE,
            powerSaveMode = false,
            lowMemory = false,
            sampledAtEpochMs = 1_000L,
        )

        val report = formatDiagnosticReport(device, snapshot, appCpuCores = 0.5)

        assertTrue(report.contains("Name: Example Model X"))
        assertTrue(report.contains("SoC: SoC Y"))
        assertTrue(report.contains("Used RAM: 75%"))
        assertTrue(report.contains("App PSS: 0.3 GB"))
        assertTrue(report.contains("App CPU: 0.50 cores"))
        assertTrue(report.contains("Battery: 75%"))
        assertTrue(report.contains("Thermal status: Moderate"))
        assertTrue(report.contains("Power saver: Off"))
        assertTrue(report.contains("Low-memory warning: No"))
    }

    @Test
    fun marksUnavailableValuesAndRejectsInvalidCpuMetric() {
        val device = DeviceReport(
            manufacturer = "",
            model = "",
            socModel = "",
            hardware = "",
            androidRelease = "",
            apiLevel = 0,
            cpuThreadCount = 0,
            totalMemoryMb = null,
            supportedAbis = emptyList(),
        )
        val snapshot = DeviceSnapshot(
            totalMemoryMb = null,
            availableMemoryMb = null,
            appPssMb = null,
            batteryPercent = null,
            batteryState = BatteryState.UNKNOWN,
            batterySource = "",
            thermalState = ThermalState.UNKNOWN,
            powerSaveMode = null,
            lowMemory = null,
            sampledAtEpochMs = 0L,
        )

        val report = formatDiagnosticReport(device, snapshot, appCpuCores = Double.NaN)

        assertTrue(report.contains("Name: Unavailable"))
        assertTrue(report.contains("Total RAM: Unavailable"))
        assertTrue(report.contains("Battery: Unavailable"))
        assertTrue(report.contains("App CPU: Unavailable"))
        assertFalse(report.contains("NaN cores"))
    }
}
