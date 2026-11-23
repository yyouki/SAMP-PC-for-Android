# SAMP-PC-for-Android

**SAMPDroid Lite** is an Android launcher/runtime integration project whose non-negotiable goal is to run the **original Windows SA-MP PC client** with a user-supplied, compatible copy of Grand Theft Auto: San Andreas for PC.

This is not an Android-native reimplementation of SA-MP, a mobile multiplayer client, or a game-streaming app.

> **Project status: planning / proof-of-concept preparation.** No Android runtime integration or successful on-device SA-MP session is claimed yet.

## Project goals

- Launch the original 32-bit Windows GTA: San Andreas executable and SA-MP PC client.
- Keep the Android launcher lightweight and inspired by the usability patterns of Zalith Launcher.
- Keep the Windows compatibility runtime modular and replaceable.
- Detect device capabilities and select only tested CPU, graphics, and input configurations.
- Measure performance on real devices, beginning with the Redmi 14C (Helio G81-Ultra / Mali-G52 MC2).
- Preserve compatibility and stability; never trade a working SA-MP session for an unverified FPS tweak.

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

See [Architecture](docs/ARCHITECTURE.md), [Roadmap](docs/ROADMAP.md), [Compatibility testing](docs/COMPATIBILITY.md), and [Architecture decisions](docs/DECISIONS.md).

## SA-MP game-file requirements

The official SA-MP download page states that SA-MP requires the PC DVD release of GTA: San Andreas v1.00 US/EU and that Steam and Direct2Drive editions are not supported by its client. Check the current official requirements before testing: <https://www.sa-mp.com/download.php>.

Users must provide their own legally obtained game files and client installer. The launcher must not download, bundle, or redistribute copyrighted game assets.

## Development principles

1. **Prove the runtime first.** A beautiful launcher cannot compensate for an incompatible 32-bit Windows execution path.
2. **Measure before optimizing.** Every performance change needs a reproducible before/after benchmark and a compatibility check.
3. **Keep device-specific tuning data-driven.** Do not hard-code a single renderer or CPU preset for all Android devices.
4. **Prefer a minimal game session.** Avoid loading a full desktop shell or unnecessary services.
5. **Pin and audit dependencies.** Record upstream URL, exact revision, license, build options, and checksums for runtime components.
6. **Be honest about support.** A configuration is supported only after it passes the documented test matrix.

## Current milestone

**Milestone 0 — Runtime feasibility.** Validate the entire chain on one Android ARM64/Mali device:

1. Android app can stage and validate user-supplied game files.
2. The chosen runtime starts a known 32-bit Windows test executable.
3. Wine can create and display a basic Windows process.
4. GTA: San Andreas PC starts and renders correctly.
5. The original SA-MP PC client starts the game and connects to a test server.

Do not mark a later step complete merely because an earlier step passes.

## License

The repository's source-code license has not yet been selected. Third-party components retain their own licenses and notices. No third-party source, binary, branding, or asset should be copied into this repository until its license and redistribution terms have been reviewed.
