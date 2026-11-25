package com.yyouki.sampdroid.device

/**
 * Basic Android device identity used by the diagnostics dashboard.
 * This model contains only public platform facts; it does not infer GPU or driver details.
 */
enum class ProcessBitness(val displayName: String) {
    BIT_32("32-bit"),
    BIT_64("64-bit"),
    UNAVAILABLE("Unavailable");

    companion object {
        fun fromIs64Bit(is64Bit: Boolean): ProcessBitness =
            if (is64Bit) BIT_64 else BIT_32
    }
}

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
    val processBitness: ProcessBitness = ProcessBitness.UNAVAILABLE,
) {
    val displayName: String
        get() = listOf(manufacturer, model)
            .filter(String::isNotBlank)
            .distinct()
            .joinToString(" ")
}
