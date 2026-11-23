package com.yyouki.sampdroid.runtime

/**
 * Runtime integration boundary. UI code must not construct Wine or shell commands.
 * Implementations are added only after a Windows x86 execution path passes the PoC.
 */
enum class RuntimeReadiness {
    NOT_INTEGRATED,
    NOT_INSTALLED,
    READY,
    BROKEN,
}

data class RuntimeStatus(
    val readiness: RuntimeReadiness,
    val message: String,
    val runtimeVersion: String? = null,
)

data class GameProfile(
    val id: String,
    val displayName: String,
    val gameDirectoryUri: String?,
    val nickname: String,
)

data class GameLaunchRequest(
    val profile: GameProfile,
    val serverAddress: String?,
)

sealed interface GameLaunchResult {
    data object RuntimeNotIntegrated : GameLaunchResult
    data class Started(val processId: Int?) : GameLaunchResult
    data class Failed(val code: String, val message: String) : GameLaunchResult
}

/**
 * Potentially blocking runtime calls are suspend functions.
 * Invoke them from an application coroutine scope, never directly from a composable.
 */
interface PcGameRuntime {
    suspend fun inspect(): RuntimeStatus
    suspend fun launch(request: GameLaunchRequest): GameLaunchResult
    suspend fun stop(): Boolean
}
