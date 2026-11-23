# Architecture

## 1. System boundary

SAMPDroid Lite has two separate responsibilities:

- **Android application:** user interface, game-file import, device detection, runtime installation, profiles, controls, logs, and process lifecycle.
- **Windows game environment:** Wine and the selected CPU-translation path execute the user's original 32-bit Windows GTA: San Andreas and SA-MP PC client.

The launcher is not the game engine. It must not replace the SA-MP PC client with a native Android networking implementation.

## 2. High-level component model

```text
Android app (ARM64)
|
+-- UI (Kotlin / Jetpack Compose)
|   +-- Home / Play
|   +-- Game profile manager
|   +-- Server favorites
|   +-- Runtime settings
|   +-- Touch / gamepad editor
|   +-- Logs and diagnostics
|
+-- Application services
|   +-- Device capability probe
|   +-- Game-file importer and validator
|   +-- Runtime manifest / installer
|   +-- Profile and settings storage
|   +-- Session and process manager
|   +-- Benchmark recorder
|
+-- Runtime adapter (stable Kotlin API)
    |
    +-- Linux userspace / display surface
    +-- Wine (Windows compatibility)
    +-- x86-on-ARM64 execution path (to be proven)
    +-- Graphics translation (device-specific)
    |
    +-- User-provided GTA: San Andreas PC
        +-- gta_sa.exe
        +-- original SA-MP PC installation
        +-- original SA-MP launcher / client files
```

The diagram is conceptual. Exact process boundaries, display integration, and runtime packaging are implementation decisions to be validated by the proof of concept.

## 3. Android application modules

### `app`
Android entry point, permissions, activity lifecycle, navigation, and dependency wiring.

### `core:device`
Reads public Android system properties and supported APIs to identify ABI, CPU features, GPU/driver information where available, Android version, memory class, and thermal status. Capability detection must be defensive: unavailable data is reported as unknown, not guessed.

### `core:storage`
Uses the Storage Access Framework for user selection. The runtime needs a stable filesystem path; therefore, test a controlled import/staging step into app-owned storage instead of assuming a SAF URI can be mounted directly into the Linux userspace.

### `launcher`
Jetpack Compose UI, game profiles, saved server addresses, and settings. The UI should remain responsive while imports, extraction, and runtime startup happen in background work.

### `runtime:api`
A small interface that hides implementation details from the UI. Suggested operations:

- inspectRuntime()
- installRuntime(profile)
- validateGameFiles(profile)
- startGame(profile, launchTarget)
- stopSession(sessionId)
- collectDiagnostics(sessionId)

The API should return structured status and error codes. Do not expose shell command construction to UI code.

### `runtime:impl`
Runtime-specific process setup, Wine prefix management, environment variables, display surface, and process monitoring. Keep this replaceable so another runtime can be evaluated without rewriting the launcher.

### `input`
Configurable touchscreen overlay and optional physical keyboard/gamepad mapping. Input must not inject cheats or alter server-authoritative gameplay; it should translate ordinary user input to the game.

### `benchmark`
Collects timestamped frame-time/FPS samples when available, process memory, startup duration, Android thermal status, selected profile, and runtime versions. A benchmark record must include enough metadata to reproduce it.

## 4. Windows execution path: an explicit feasibility gate

The target Windows binaries are 32-bit x86. Android devices such as the Redmi 14C use ARM64. Wine alone does not translate CPU instructions; a compatible x86-on-ARM64 path is required.

Candidates to test independently:

1. **Box86 with a compatible 32-bit ARM userspace.** Verify that the target Android/kernel and packaged userspace can execute the required 32-bit ARM components.
2. **Box64 with x86-64 Wine WoW64.** Verify the exact Wine build, 32-bit Windows support, graphics stack, and SA-MP client behavior.
3. **Box64 Box32 mode or ARM64 Wine WoW64.** Treat these as experimental candidates until the complete game/client chain is stable.

Do not assume these paths are interchangeable. Record exact source revisions, build options, ABI requirements, and test outcomes. The first milestone is to execute a small known 32-bit Windows test program; the next is a basic Wine GUI program; only then attempt GTA: San Andreas.

A successful test of `gta_sa.exe` is not proof that the SA-MP launcher or its client initialization works. The complete original SA-MP startup path must be tested.

## 5. Graphics path for Mali

The initial reference device has a Mali-G52 MC2 GPU. Do not select the Qualcomm Turnip driver for this device: Turnip is an Adreno-focused Mesa driver.

Treat graphics configuration as a capability-based matrix, not a single hard-coded preset:

- Baseline candidate: WineD3D with a Mali-compatible OpenGL path (for example, a tested VirGL-based configuration).
- Experimental candidate: D3D9-to-Vulkan translation only when the device's Vulkan driver exposes the required capabilities and the runtime can access it correctly.
- Fallback: a lower-resolution or reduced-effects profile that has been measured on the device.

Do not use a software rasterizer as a claimed performance optimization. It can be useful for diagnosis, but it is generally not an appropriate real-time 3D gaming path.

GTA: San Andreas uses a Direct3D 9-era rendering path. The selected Wine graphics configuration must be validated with the actual game, not only with a generic Direct3D test.

## 6. Minimal game session

The goal is not to recreate a full desktop PC. Avoid loading components that the game does not need:

- no full desktop shell unless a specific game workflow requires it;
- no background update daemon during gameplay;
- no duplicate runtime/container for a single active game profile;
- no always-on telemetry;
- no unnecessary services or overlays;
- no repeated extraction or shader/cache rebuilding when a valid cache is present.

Do not remove runtime libraries simply because they appear unused. Remove a component only after dependency tracing and regression testing.

## 7. Launch lifecycle

1. User selects a game profile and presses **Play**.
2. Launcher checks game-file presence, integrity markers, runtime compatibility, free storage, and any pending import.
3. Device capability probe chooses a known tested profile; unknown devices use a conservative fallback.
4. Runtime adapter prepares the Wine prefix and display/input configuration.
5. Process manager starts the original SA-MP PC launch target using the verified launch sequence.
6. Launcher monitors the process tree and session state without polling aggressively.
7. On exit or crash, collect opt-in diagnostics, flush settings, and release the session resources.

The exact executable sequence must be established from a legally obtained SA-MP client installation and runtime experiments. Do not assume launching `samp.exe` directly is sufficient for every client revision.

## 8. Security and data handling

- User selects and supplies their own game files and installer.
- Validate paths and required file presence; never execute a file merely because it exists in an imported archive.
- Keep downloaded runtime packages pinned to a trusted release/revision and verify SHA-256 checksums.
- Do not run arbitrary shell fragments from profile metadata.
- Do not expose Wine prefixes or app-private files to unrelated apps.
- Keep diagnostics local by default; ask before sharing logs because they may contain usernames, paths, server addresses, or device details.
- Third-party license notices must ship with redistributed runtime components.

## 9. Performance engineering loop

Every optimization follows this loop:

1. Capture a baseline using the same device, game build, runtime, scene, resolution, and thermal conditions.
2. Change one variable.
3. Repeat the test enough times to identify noise and regression.
4. Compare frame-time distribution, sustained performance, memory, temperature, startup time, and crash rate.
5. Run the SA-MP connection smoke test.
6. Keep the change only if the improvement is reproducible and compatibility remains intact.

No performance claim is valid without device, build, profile, test conditions, and measurement method.
