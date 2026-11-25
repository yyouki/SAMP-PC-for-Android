package com.yyouki.sampdroid.device

const val MIN_CPU_INTERVAL_MS = 2_000L

class AppCpuTracker(private val minIntervalMs: Long = MIN_CPU_INTERVAL_MS) {
    private var baseline: CpuTimeSample? = null
    private var latestCores: Double? = null

    fun reset() {
        baseline = null
        latestCores = null
    }

    fun record(current: CpuTimeSample): Double? {
        val previous = baseline
        if (previous == null) {
            baseline = current
            return null
        }

        if (current.elapsedRealtimeMs - previous.elapsedRealtimeMs < minIntervalMs) return latestCores

        latestCores = calculateAppCpuCores(previous, current)
        baseline = current
        return latestCores
    }
}
