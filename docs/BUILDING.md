# Building SAMPDroid Lite

## Toolchain

| Component | Version |
|---|---:|
| Android Gradle Plugin | 9.4.0 |
| Gradle Wrapper | 9.7.0 |
| AGP built-in Kotlin / Compose compiler plugin | Kotlin 2.4.21 |
| Jetpack Compose BOM | 2026.09.00 |
| AndroidX Activity Compose | 1.13.0 |
| AndroidX Lifecycle Runtime Compose | 2.11.1 |
| JDK | 17 |
| Compile SDK | Android API 37.1 (CANARY) |
| Target SDK | 36 |
| Minimum SDK | 26 |

AGP 9.4 supports Android API 37 and requires Gradle 9.6.0 or newer and JDK 17. This project uses Gradle 9.7.0 and JDK 17. Kotlin compilation uses AGP's built-in Kotlin support; the matching Compose compiler plugin is pinned to Kotlin 2.4.21. The legacy `org.jetbrains.kotlin.android` plugin is not applied because it conflicts with AGP 9's new Android DSL.

Compose 1.13 requires compile SDK 37.1. The SDK repository publishes that preview platform as `platforms;android-CANARY`; the CI workflow installs this package from the Canary channel. The app keeps target SDK 36.

## Build a debug APK

From the repository root:

```bash
./gradlew --no-daemon :app:assembleDebug
```

On Windows:

```bat
gradlew.bat --no-daemon :app:assembleDebug
```

The debug APK is generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Run unit tests

```bash
./gradlew --no-daemon :app:testDebugUnitTest
```

## Dashboard scope

The app displays device identity, total/available memory, app PSS, battery capacity/state/source, Android thermal status, power-save state, and low-memory warnings. It estimates CPU core-equivalents for this app process from Process CPU time; this is not system-wide CPU utilization. Sampling pauses when the activity is not visible. Android does not expose a universal, reliable per-device GPU/driver string through a single public API; that field is not guessed.

The light/dark theme preference is stored locally in app-private SharedPreferences. It is read at startup and updated only when the user changes the theme.

The user can explicitly share a plain-text diagnostic report using Android Sharesheet. The report contains device characteristics and sampled telemetry. Sharing does not happen automatically; the user selects the destination and should review the report first.

## CI artifacts

A successful GitHub Actions run uploads `app/build/outputs/apk/debug/app-debug.apk` as `sampdroid-lite-debug-<commit-sha>`. Artifacts are retained for seven days and can be downloaded from the workflow run's **Artifacts** section.

## Reproducibility

- Gradle Wrapper uses a pinned distribution URL and SHA-256 checksum.
- Plugin and library versions are fixed in the version catalog.
- CI installs Android API 37.1 as `platforms;android-CANARY` and Build Tools 36.0.0.
- CI runs `:app:assembleDebug` and `:app:testDebugUnitTest` before uploading the APK.
- Do not use dynamic dependency versions such as `9.+` or `latest.release`.
