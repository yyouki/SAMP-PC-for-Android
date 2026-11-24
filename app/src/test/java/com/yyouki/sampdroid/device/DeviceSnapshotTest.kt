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
    fun formatsMemoryUsingStableUnitAndLocale() {
        assertEquals("1.0 GB", formatMemoryMb(1_024))
        assertEquals("Unavailable", formatMemoryMb(null))
        assertEquals("Unavailable", formatMemoryMb(-1))
    }
}
