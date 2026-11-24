# Architecture

## Current application scope

SAMPDroid Lite is currently an Android device diagnostics application built with Kotlin and Jetpack Compose. The repository's implemented functionality is limited to Android UI, public device telemetry, local preferences, diagnostic report formatting, and CI build/test automation.

No external execution runtime is packaged or integrated in the current application.

## App modules

### Android application

- Owns the Activity lifecycle and Compose UI.
- Applies the locally saved light/dark theme preference.
- Hosts the device dashboard and runtime integration status display.
- Uses Android's Sharesheet only after the user explicitly requests a diagnostic report.

### Device diagnostics

- Reads public Android device information and supported ABI values.
- Samples RAM, app PSS, battery state, thermal status, power-saving state, and low-memory warning.
- Samples this app process's CPU time and displays a core-equivalent estimate.
- Pauses periodic sampling when the Activity is in the background.
- Reports unavailable values as unavailable instead of inferring or fabricating them.

### Runtime status

The `runtime` package currently exposes a read-only status model. It reports whether an external runtime is integrated into this build. It does not install, load, execute, or control external programs.

The UI must not contain shell-command construction or external process controls.

### Diagnostic report

The user can explicitly share a plain-text report containing device characteristics and current measurements. The report is not sent automatically. The user chooses the destination in Android Sharesheet and should review the content before sharing it.

## Design principles

1. Keep Android UI responsive and lifecycle-aware.
2. Use public platform APIs and handle unavailable data explicitly.
3. Keep device sampling lightweight and pause it when the Activity is not visible.
4. Keep user preferences in app-private storage.
5. Do not collect or share diagnostic data without an explicit user action.
6. Keep the build reproducible with pinned dependencies and CI validation.
7. Make implementation status visible; do not present unimplemented capabilities as working features.

## Verification

- Unit tests cover pure telemetry calculations and diagnostic report formatting.
- Runtime status has a unit test verifying the current not-integrated state.
- GitHub Actions builds the debug APK and runs unit tests before uploading the artifact.
