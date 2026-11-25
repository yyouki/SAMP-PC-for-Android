# Roadmap

This roadmap covers the currently supported Android diagnostics application. Items are considered complete only after implementation and verification.

## Milestone 1 — Android dashboard foundation

- [x] Kotlin and Jetpack Compose application scaffold.
- [x] Device identity and supported ABI display.
- [x] RAM, app PSS, battery, thermal, and power-saving telemetry.
- [x] Foreground-only sampling lifecycle.
- [x] Per-process CPU time estimate with clear labeling.
- [x] Runtime integration status displayed as not integrated.
- [x] Android app-process bitness shown separately from device-supported ABIs.
- [x] Light/dark themes with local persistence.
- [x] User-initiated diagnostic report sharing.

## Milestone 2 — Reliability and presentation

- [ ] Verify layout on compact, standard, and large Android displays.
- [ ] Add accessibility labels and verify screen-reader navigation.
- [ ] Add UI tests for theme switching and scroll behavior.
- [ ] Test behavior through Activity recreation and process restoration.
- [ ] Review sampling overhead on a physical Android device.
- [ ] Verify diagnostic report formatting with unavailable telemetry fields.

## Milestone 3 — Device verification

- [ ] Install the debug APK on a physical Redmi 14C.
- [ ] Compare displayed model, Android version, ABI, and memory data with device settings.
- [ ] Verify background sampling pauses and resumes correctly.
- [ ] Verify theme preference survives force-stop and restart.
- [ ] Verify the Sharesheet opens and the report is readable before sending.
- [ ] Record OS version, test date, and reproducible observations.

## Milestone 4 — Build and release hygiene

- [x] Pin Gradle and dependency versions.
- [x] Run Android build and unit tests in GitHub Actions.
- [x] Upload debug APK artifacts from successful CI runs.
- [ ] Add static analysis and formatting checks.
- [ ] Select and document the repository source license.
- [ ] Review third-party dependency licenses and notices.

## Runtime integration status

External runtime integration is not part of the current implementation. The app exposes a read-only status that says **Not integrated**; it does not launch or manage external executables. A successful Android dashboard build must not be interpreted as proof of external runtime compatibility.
