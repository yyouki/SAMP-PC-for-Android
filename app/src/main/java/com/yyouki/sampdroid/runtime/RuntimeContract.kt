package com.yyouki.sampdroid.runtime

/**
 * Read-only integration status for the Android dashboard.
 *
 * This module deliberately exposes status only. It does not install, load,
 * execute, or control external programs.
 */
enum class RuntimeIntegrationState {
    NOT_INTEGRATED,
}

data class RuntimeStatus(
    val state: RuntimeIntegrationState,
    val title: String,
    val description: String,
)

/** Returns the truthful status of the current app build. */
fun currentRuntimeStatus(): RuntimeStatus = RuntimeStatus(
    state = RuntimeIntegrationState.NOT_INTEGRATED,
    title = "Not integrated",
    description = "This build contains no external runtime components.",
)
