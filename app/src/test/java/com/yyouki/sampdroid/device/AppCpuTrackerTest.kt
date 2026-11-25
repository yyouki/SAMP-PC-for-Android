package com.yyouki.sampdroid.device

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AppCpuTrackerTest {
    private fun sample(cpuMs: Long, wallMs: Long) = CpuTimeSample(
        processCpuTimeMs = cpuMs,
        elapsedRealtimeMs = wallMs,
    )

    @Test
    fun firstSampleOnlyEstablishesBaseline() {
        val tracker = AppCpuTracker()

        assertNull(tracker.record(sample(cpuMs = 1_000, wallMs = 10_000)))
    }

    @Test
    fun usesActualElapsedTimeBetweenSamples() {
        val tracker = AppCpuTracker()

        tracker.record(sample(cpuMs = 1_000, wallMs = 10_000))

        assertEquals(0.5, tracker.record(sample(cpuMs = 2_500, wallMs = 13_000))!!, 0.0001)
        assertEquals(0.25, tracker.record(sample(cpuMs = 4_250, wallMs = 20_000))!!, 0.0001)
    }

    @Test
    fun keepsPreviousValueAndBaselineWhenSampleIsTooClose() {
        val tracker = AppCpuTracker()

        tracker.record(sample(cpuMs = 1_000, wallMs = 1_000))
        assertEquals(0.5, tracker.record(sample(cpuMs = 3_500, wallMs = 6_000))!!, 0.0001)

        assertEquals(0.5, tracker.record(sample(cpuMs = 3_600, wallMs = 6_500))!!, 0.0001)
        assertEquals(0.25, tracker.record(sample(cpuMs = 4_750, wallMs = 11_000))!!, 0.0001)
    }

    @Test
    fun staysUnavailableWhenFirstIntervalIsTooShort() {
        val tracker = AppCpuTracker()

        tracker.record(sample(cpuMs = 0, wallMs = 0))
        assertNull(tracker.record(sample(cpuMs = 50, wallMs = 100)))
        assertEquals(0.25, tracker.record(sample(cpuMs = 1_250, wallMs = 5_000))!!, 0.0001)
    }

    @Test
    fun acceptsSampleExactlyAtMinimumInterval() {
        val tracker = AppCpuTracker()

        tracker.record(sample(cpuMs = 0, wallMs = 0))

        assertNull(tracker.record(sample(cpuMs = 1_000, wallMs = MIN_CPU_INTERVAL_MS - 1)))
        assertEquals(0.5, tracker.record(sample(cpuMs = 1_000, wallMs = MIN_CPU_INTERVAL_MS))!!, 0.0001)
    }

    @Test
    fun resetDiscardsBaselineAndPreviousValue() {
        val tracker = AppCpuTracker()

        tracker.record(sample(cpuMs = 0, wallMs = 0))
        assertEquals(0.5, tracker.record(sample(cpuMs = 2_500, wallMs = 5_000))!!, 0.0001)

        tracker.reset()

        assertNull(tracker.record(sample(cpuMs = 9_000, wallMs = 6_000)))
        assertEquals(0.5, tracker.record(sample(cpuMs = 10_000, wallMs = 8_000))!!, 0.0001)
    }

    @Test
    fun ignoresSamplesThatMoveBackwardsInTime() {
        val tracker = AppCpuTracker()

        tracker.record(sample(cpuMs = 0, wallMs = 10_000))
        assertEquals(0.5, tracker.record(sample(cpuMs = 2_500, wallMs = 15_000))!!, 0.0001)

        assertEquals(0.5, tracker.record(sample(cpuMs = 9_999, wallMs = 12_000))!!, 0.0001)
        assertEquals(0.25, tracker.record(sample(cpuMs = 3_750, wallMs = 20_000))!!, 0.0001)
    }

    @Test
    fun clearsValueAndStartsNewBaselineWhenCpuTimeGoesBackwards() {
        val tracker = AppCpuTracker()

        tracker.record(sample(cpuMs = 1_000, wallMs = 0))
        assertEquals(0.5, tracker.record(sample(cpuMs = 3_500, wallMs = 5_000))!!, 0.0001)

        assertNull(tracker.record(sample(cpuMs = 3_000, wallMs = 10_000)))
        assertEquals(0.25, tracker.record(sample(cpuMs = 4_250, wallMs = 15_000))!!, 0.0001)
    }

    @Test
    fun respectsCustomMinimumInterval() {
        val tracker = AppCpuTracker(minIntervalMs = 500)

        tracker.record(sample(cpuMs = 0, wallMs = 0))

        assertEquals(0.3, tracker.record(sample(cpuMs = 150, wallMs = 500))!!, 0.0001)
    }
}
