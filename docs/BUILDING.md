# Building SAMPDroid Lite

## Toolchain

| Component | Version |
|---|---:|
| Android Gradle Plugin | 9.4.0 |
| Gradle Wrapper | 9.7.0 |
| Kotlin Gradle Plugin | 2.4.21 |
| Jetpack Compose BOM | 2026.09.00 |
| AndroidX Activity Compose | 1.13.0 |
| AndroidX Lifecycle Runtime Compose | 2.11.1 |
| JDK | 17 |
| Compile SDK | Android API 36 |
| Target SDK | 36 |
| Minimum SDK | 26 |

AGP 9.4 requires Gradle 9.6.0 or newer and JDK 17. This project uses Gradle 9.7.0 and JDK 17. Built-in Kotlin is temporarily disabled because the project explicitly pins Kotlin Gradle Plugin 2.4.21 and the matching Compose compiler plugin.

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

The app displays device identity, memory telemetry, battery level, Android thermal status, and power-save state. Sampling is paused when the activity is not visible. Android does not expose a universal, reliable per-device GPU/driver string or system-wide CPU utilization through a single public API; those fields are not guessed.

## Reproducibility

- Gradle Wrapper uses a pinned distribution URL and SHA-256 checksum.
- Plugin and library versions are fixed in the version catalog.
- CI uses stable Android API 36 and Build Tools 36.0.0.
- Do not use dynamic dependency versions such as `9.+` or `latest.release`.
