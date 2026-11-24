# Building SAMPDroid Lite

## Toolchain

| Component | Version |
|---|---:|
| Android Gradle Plugin | 9.4.0 |
| Gradle Wrapper | 9.7.0 |
| AGP built-in Kotlin | Kotlin 2.4.21 |
| Jetpack Compose BOM | 2026.09.00 |
| AndroidX Activity Compose | 1.13.0 |
| AndroidX Lifecycle Runtime Compose | 2.11.1 |
| JDK | 17 |
| Compile SDK | Android API 36 |
| Target SDK | 36 |
| Minimum SDK | 26 |

AGP 9.4 requires Gradle 9.6.0 or newer and JDK 17. This project uses Gradle 9.7.0 and JDK 17. Kotlin is provided by AGP's built-in Kotlin support, while the Compose compiler plugin is pinned to Kotlin 2.4.21. The legacy org.jetbrains.kotlin.android plugin is intentionally not applied because it is incompatible with AGP 9's new Android DSL.

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

## Reproducibility

- Gradle Wrapper uses a pinned distribution URL and SHA-256 checksum.
- Plugin and library versions are fixed in the version catalog.
- CI uses stable Android API 36 and Build Tools 36.0.0.
- Do not use dynamic dependency versions such as `9.+` or `latest.release`.
