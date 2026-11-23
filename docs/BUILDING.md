# Building SAMPDroid Lite

## Toolchain

The initial Android shell uses this pinned toolchain:

| Component | Version |
|---|---:|
| Android Gradle Plugin | 9.3.1 |
| Gradle Wrapper | 9.7.0 |
| Kotlin | 2.4.21 |
| Jetpack Compose BOM | 2026.11.01 |
| AndroidX Activity Compose | 1.13.0 |
| JDK | 17 |
| Compile SDK | Android API 37, minor API 1 |
| Target SDK | 36 |
| Minimum SDK | 26 |

Install Android SDK Platform API 37.1 and Build Tools 36.0.0 from Android Studio's SDK Manager. Use an Android Studio version that supports API 37.1.

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

## Current application scope

This first APK is an Android launcher shell and device-information screen. It can display basic device facts such as model, SoC identifier, RAM capacity, CPU thread count, Android API level, and supported ABIs.

It does not yet contain Wine, Box86/Box64/Box32, a Windows display server, a Direct3D renderer, GTA: San Andreas, or the SA-MP PC client. The Play button remains disabled until a validated runtime implementation exists.

The GPU line is intentionally marked "Probe not implemented". Reading the GPU model reliably requires a graphics-context probe; the app must not infer the active renderer from the SoC name.

## Reproducibility and supply chain

- Gradle Wrapper uses a pinned distribution URL and the official Gradle 9.7.0 SHA-256 checksum.
- Plugin and library versions are fixed in the Gradle version catalog.
- Do not add dynamic dependency versions such as 9.+ or latest.release.
- Runtime components must later have pinned source revisions, license notices, build options, and checksums.
