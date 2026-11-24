# SAMP-PC-for-Android

**SAMPDroid Lite** is an Android launcher/runtime integration project whose non-negotiable goal is to run the **original Windows SA-MP PC client** with a user-supplied, compatible copy of Grand Theft Auto: San Andreas for PC.

This is not an Android-native reimplementation of SA-MP, a mobile multiplayer client, or a game-streaming app.

> **Project status:** Android launcher shell scaffolded. Windows runtime integration and successful on-device SA-MP PC execution are **not yet implemented or verified**.

## Project goals

- Launch the original 32-bit Windows GTA: San Andreas executable and SA-MP PC client.
- Keep the Android launcher lightweight and inspired by the usability patterns of Zalith Launcher.
- Keep the Windows compatibility runtime modular and replaceable.
- Detect device capabilities and select only tested CPU, graphics, and input configurations.
- Measure performance on real devices, beginning with the Redmi 14C (Helio G81-Ultra / Mali-G52 MC2).
- Preserve compatibility and stability; never trade a working SA-MP session for an unverified FPS tweak.

## Current implementation

- Android project scaffold using Kotlin and Jetpack Compose.
- Device Performance Dashboard built with Jetpack Compose.
- Device identity report: manufacturer, model, SoC identifier, hardware, Android version, CPU thread count, and supported ABIs.
- Foreground-only telemetry sampling for total/available RAM, app PSS, battery capacity/state/source, thermal status, power-save state, and low-memory warning.
- Per-process CPU consumption displayed as core-equivalents using monotonic CPU-time sampling; this is not system-wide CPU utilization.
- Pure unit tests for memory calculations, app CPU interval calculations, thermal/battery state mapping, and metric formatting.
- The dashboard explicitly avoids guessing GPU drivers or system-wide CPU utilization.
- The runtime contract remains separate from the UI; no game runtime is integrated by this dashboard.
- Gradle Wrapper configuration, pinned build dependencies, build instructions, and an Android CI workflow.

The current app is a **launcher shell**, not a working SA-MP runner. GPU/driver probing is not implemented yet.

## Non-goals

- Replacing the original SA-MP PC client with a native Android multiplayer implementation.
- Bundling copyrighted GTA: San Andreas game data or redistributing proprietary installers.
- Promising a particular FPS or claiming that a device is supported before reproducible testing.
- Shipping a general-purpose Windows desktop environment when the game-only runtime can meet the requirement.

## Technical direction

The Android app is the **launcher and runtime manager**. The Windows game remains a 32-bit x86 application. The project must therefore prove a compatible path for Windows x86 execution on Android ARM64 before investing in a complete UI.

Candidate components (not yet locked):

- Android UI and application layer: Kotlin, Jetpack Compose, Material 3.
- Android/Windows boundary: a small, testable runtime interface and JNI only where required.
- Windows compatibility: Wine, with a tested 32-bit x86-on-ARM64 execution path.
- CPU translation candidates: Box86 in a suitable 32-bit ARM userspace, Box64 with Wine WoW64, or another demonstrably compatible approach. Box32 is experimental and must not be treated as a production default without testing.
- Graphics: choose a backend using real GPU/driver capability checks. Turnip is an Adreno driver and is not a Mali solution. WineD3D/VirGL and Vulkan-based options must be measured on the target GPU rather than assumed to work.
- Input: configurable touchscreen overlay, keyboard, and gamepad mapping.

See [Architecture](docs/ARCHITECTURE.md), [Roadmap](docs/ROADMAP.md), [Compatibility testing](docs/COMPATIBILITY.md), [Architecture decisions](docs/DECISIONS.md), and [Build instructions](docs/BUILDING.md).

## SA-MP game-file requirements

The official SA-MP download page states that SA-MP requires the PC DVD release of GTA: San Andreas v1.00 US/EU and that Steam and Direct2Drive editions are not supported by its client. Check the current official requirements before testing: https://www.sa-mp.com/download.php.

Users must provide their own legally obtained game files and client installer. The launcher must not download, bundle, or redistribute copyrighted game assets.

## Build

Install JDK 17 and Android SDK Platform API 36 / Build Tools 36.0.0, then run:

```bash
./gradlew :app:assembleDebug
```

See [docs/BUILDING.md](docs/BUILDING.md) for exact versions and CI details.

## Development principles

1. **Prove the runtime first.** A beautiful launcher cannot compensate for an incompatible 32-bit Windows execution path.
2. **Measure before optimizing.** Every performance change needs a reproducible before/after benchmark and a compatibility check.
3. **Keep device-specific tuning data-driven.** Do not hard-code a single renderer or CPU preset for all Android devices.
4. **Prefer a minimal game session.** Avoid loading a full desktop shell or unnecessary services.
5. **Pin and audit dependencies.** Record upstream URL, exact revision, license, build options, and checksums for runtime components.
6. **Be honest about support.** A configuration is supported only after it passes the documented test matrix.

## Milestones

- **Android dashboard shell:** scaffolded; CI build/test verification pending.
- **Milestone 0 — Runtime feasibility:** not yet passed. We must validate a 32-bit Windows test program, Wine GUI, GTA: San Andreas PC, the original SA-MP client, and server connection on an Android ARM64/Mali device.
- **Runtime integration:** blocked until a compatible x86 execution path is proven.

Do not mark a later step complete merely because an earlier step passes.

## License

The repository's source-code license has not yet been selected. Third-party components retain their own licenses and notices. No third-party source, binary, branding, or asset should be copied into this repository until its license and redistribution terms have been reviewed.
