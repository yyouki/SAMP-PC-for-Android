# SAMP-PC-for-Android

**SAMPDroid Lite** is an Android launcher/runtime integration project whose intended product direction is documented separately from the currently implemented Android diagnostics shell.

> **Current implementation status:** The working Android app is a device diagnostics dashboard. No Windows compatibility runtime or SA-MP PC runtime is included or verified.

## Current implementation

- Android project scaffold using Kotlin and Jetpack Compose.
- Device Performance Dashboard with device identity, SoC identifier, hardware, Android version, CPU thread count, and supported ABIs.
- Foreground-only telemetry for total/available RAM, app PSS, battery capacity/state/source, thermal status, power-save state, and low-memory warning.
- Per-process CPU consumption displayed as core-equivalents using monotonic CPU-time sampling; this is not system-wide CPU utilization.
- Explicit user-initiated sharing of a plain-text diagnostic report through Android Sharesheet. The report includes device details and sampled telemetry; review it before choosing a destination.
- Pure unit tests for memory calculations, app CPU interval calculations, thermal/battery state mapping, formatting, and missing-value handling.
- The dashboard does not guess GPU drivers or system-wide CPU utilization.
- Gradle Wrapper configuration, pinned build dependencies, build instructions, and Android CI with debug APK artifact upload.

The current app is a device diagnostics shell. A successful Android build does not establish compatibility with unrelated desktop software. GPU/driver probing is not implemented.

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
- **Debug APK artifact:** uploaded by CI for each successful run; artifacts expire after seven days.
- **On-device verification:** not yet performed on a physical Redmi 14C.
- **GPU/driver telemetry:** not implemented; values are not inferred or fabricated.

## License

The repository's source-code license has not yet been selected. Third-party components retain their own licenses and notices. No third-party source, binary, branding, or asset should be copied into this repository until its license and redistribution terms have been reviewed.
