package com.yyouki.sampdroid.device

/**
 * Basic Android device identity used by the diagnostics dashboard.
 * This model contains only public platform facts; it does not infer GPU or driver details.
 */
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
) {
    val displayName: String
        get() = listOf(manufacturer, model)
            .filter(String::isNotBlank)
            .distinct()
            .joinToString(" ")
}
