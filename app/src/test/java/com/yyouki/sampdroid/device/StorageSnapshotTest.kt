package com.yyouki.sampdroid.device

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class StorageSnapshotTest {
    @Test
    fun calculatesUsedStoragePercentage() {
        assertEquals(75, calculateStorageUsagePercent(1_000L, 250L))
        assertEquals(0, calculateStorageUsagePercent(1_000L, 1_200L))
        assertEquals(100, calculateStorageUsagePercent(1_000L, 0L))
    }

    @Test
    fun returnsNullForInvalidStorageMeasurements() {
        assertNull(calculateStorageUsagePercent(0L, 0L))
        assertNull(calculateStorageUsagePercent(-1L, 0L))
        assertNull(calculateStorageUsagePercent(1_000L, -1L))
    }

    @Test
    fun formatsBytesUsingBinaryGigabytes() {
        assertEquals("1.00 GB", formatStorageBytes(1_073_741_824L))
        assertEquals("Unavailable", formatStorageBytes(null))
        assertEquals("Unavailable", formatStorageBytes(-1L))
    }

    @Test
    fun derivesUsedBytesWithoutNegativeResults() {
        assertEquals(750L, StorageSnapshot(1_000L, 250L).usedBytes)
        assertEquals(0L, StorageSnapshot(1_000L, 1_200L).usedBytes)
    }
}
