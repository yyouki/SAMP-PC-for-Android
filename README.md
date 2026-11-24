# SAMP-PC-for-Android

**SAMPDroid Lite** is an Android diagnostics application built with Kotlin and Jetpack Compose.

> **Current implementation status:** The working Android app is a device diagnostics dashboard. No Windows compatibility runtime or SA-MP PC runtime is included or verified.

## Current implementation

- Device Performance Dashboard with device identity, SoC identifier, hardware, Android version, CPU thread count, and supported ABIs.
- Foreground-only telemetry for total/available RAM, app PSS, battery capacity/state/source, thermal status, power-save state, and low-memory warning.
- Per-process CPU consumption displayed as core-equivalents using monotonic CPU-time sampling; this is not system-wide CPU utilization.
- User-selectable light and dark themes, with the selection persisted across app restarts.
- Explicit user-initiated sharing of a plain-text diagnostic report through Android Sharesheet. The report includes device details and sampled telemetry; review it before choosing a destination.
- Read-only runtime integration status card. The current state is **Not integrated**; no external program is loaded or started.
- Unit tests for memory calculations, app CPU interval calculations, thermal/battery state mapping, formatting, missing-value handling, and runtime status.
- The dashboard does not guess GPU drivers or system-wide CPU utilization.
- Gradle Wrapper configuration, pinned build dependencies, build instructions, and Android CI with debug APK artifact upload.

## Build

Install JDK 17 and Android SDK Platform API 37.1 (CANARY) plus Build Tools 36.0.0, then run:

```bash
./gradlew --no-daemon :app:assembleDebug
```

Run unit tests:

```bash
./gradlew --no-daemon :app:testDebugUnitTest
```

See [docs/BUILDING.md](docs/BUILDING.md) for exact versions and CI details. Successful CI runs upload `app-debug.apk` as a downloadable artifact for seven days.

## Diagnostic report privacy

The report is created only after the user taps **SHARE DIAGNOSTIC REPORT**. It contains public device characteristics (such as manufacturer, model, Android version, and supported ABIs) and current system/app measurements. Android then displays its Sharesheet so the user can choose the destination. Review the text before sharing it.

## Project status

- **Android diagnostics dashboard:** implemented; build and unit tests verified by GitHub Actions.
- **Theme preference:** light/dark selection persists across launches.
- **Runtime status:** read-only status model; external runtime not integrated.
- **Debug APK artifact:** uploaded by CI for each successful run; artifacts expire after seven days.
- **On-device verification:** not yet performed on a physical Redmi 14C.
- **GPU/driver telemetry:** not implemented; values are not inferred or fabricated.

## License

The repository's source-code license has not yet been selected. Third-party components retain their own licenses and notices. No third-party source, binary, branding, or asset should be copied into this repository until its license and redistribution terms have been reviewed.
