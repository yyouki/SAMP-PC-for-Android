package com.yyouki.sampdroid.device

import org.junit.Assert.assertEquals
import org.junit.Test

class DeviceReportTest {
    @Test
    fun maps64BitAndroidProcess() {
        assertEquals(ProcessBitness.BIT_64, ProcessBitness.fromIs64Bit(true))
    }

    @Test
    fun maps32BitAndroidProcess() {
        assertEquals(ProcessBitness.BIT_32, ProcessBitness.fromIs64Bit(false))
    }
}
