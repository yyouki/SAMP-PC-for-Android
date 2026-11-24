package com.yyouki.sampdroid.device

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DeviceSnapshotTest {
    @Test
    fun calculatesUsedMemoryPercentage() {
        assertEquals(50, calculateUsagePercent(totalMb = 8_000, availableMb = 4_000))
    }

    @Test
    fun clampsMemoryPercentageToValidRange() {
        assertEquals(0, calculateUsagePercent(totalMb = 8_000, availableMb = 9_000))
        assertEquals(100, calculateUsagePercent(totalMb = 8_000, availableMb = 0))
    }

    @Test
    fun returnsNullWhenMemoryDataIsInvalid() {
        assertNull(calculateUsagePercent(totalMb = null, availableMb = 100))
        assertNull(calculateUsagePercent(totalMb = 0, availableMb = 0))
    }

    @Test
    fun calculatesAppCpuAsCoreEquivalents() {
        val previous = CpuTimeSample(processCpuTimeMs = 1_000, elapsedRealtimeMs = 5_000)
        val current = CpuTimeSample(processCpuTimeMs = 1_500, elapsedRealtimeMs = 6_000)

        assertEquals(0.5, calculateAppCpuCores(previous, current)!!, 0.0001)
    }

    @Test
    fun returnsNullForMissingOrInvalidCpuIntervals() {
        val current = CpuTimeSample(processCpuTimeMs = 1_500, elapsedRealtimeMs = 6_000)
        val backwards = CpuTimeSample(processCpuTimeMs = 900, elapsedRealtimeMs = 6_000)

        assertNull(calculateAppCpuCores(null, current))
        assertNull(calculateAppCpuCores(current, backwards))
    }

    @Test
    fun mapsAndroidThermalStatuses() {
        assertEquals(ThermalState.NONE, ThermalState.fromPlatformStatus(0))
        assertEquals(ThermalState.LIGHT, ThermalState.fromPlatformStatus(1))
        assertEquals(ThermalState.MODERATE, ThermalState.fromPlatformStatus(2))
        assertEquals(ThermalState.SEVERE, ThermalState.fromPlatformStatus(3))
        assertEquals(ThermalState.CRITICAL, ThermalState.fromPlatformStatus(4))
        assertEquals(ThermalState.EMERGENCY, ThermalState.fromPlatformStatus(5))
        assertEquals(ThermalState.SHUTDOWN, ThermalState.fromPlatformStatus(6))
        assertEquals(ThermalState.UNKNOWN, ThermalState.fromPlatformStatus(-1))
    }

    @Test
    fun mapsBatteryStatuses() {
        assertEquals(BatteryState.CHARGING, BatteryState.fromPlatformStatus(2))
        assertEquals(BatteryState.DISCHARGING, BatteryState.fromPlatformStatus(3))
        assertEquals(BatteryState.NOT_CHARGING, BatteryState.fromPlatformStatus(4))
        assertEquals(BatteryState.FULL, BatteryState.fromPlatformStatus(5))
        assertEquals(BatteryState.UNKNOWN, BatteryState.fromPlatformStatus(-1))
    }

    @Test
    fun formatsMemoryUsingStableUnitAndLocale() {
        assertEquals("1.0 GB", formatMemoryMb(1_024))
        assertEquals("Unavailable", formatMemoryMb(null))
        assertEquals("Unavailable", formatMemoryMb(-1))
    }
}
