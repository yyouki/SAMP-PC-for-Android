# Roadmap

This roadmap is gated by test evidence. A milestone is complete only when its acceptance criteria are recorded in the repository.

## Milestone 0 — Runtime feasibility

**Purpose:** prove that the original 32-bit Windows SA-MP PC client can run on at least one Android ARM64 device.

### Tasks

- [ ] Record target device model, Android build, ABI, RAM variant, SoC, GPU, and driver information.
- [ ] Build a minimal runtime test harness, separate from the full launcher UI.
- [ ] Select and document one x86-on-ARM64 candidate path.
- [ ] Run a known 32-bit Windows console test program.
- [ ] Run a basic Wine GUI test program.
- [ ] Establish a display path and capture a screenshot.
- [ ] Launch user-provided GTA: San Andreas PC v1.00 US/EU.
- [ ] Install and launch the original SA-MP PC client.
- [ ] Connect to a controlled test server and verify player movement and basic synchronization.
- [ ] Save logs, runtime revisions, checksums, settings, and test results.

### Acceptance criteria

- A full cold start reaches the SA-MP PC client without relying on a separate Windows PC or game-streaming service.
- The game renders and accepts input for a continuous test session.
- The original SA-MP client connects to a compatible server.
- The session can exit cleanly and can be started again.
- Known failures and limitations are documented.

**Stop condition:** if the Windows x86 execution path or SA-MP initialization cannot be made reliable, do not proceed to polish the launcher UI as though the core product works. Revisit the runtime candidate.

## Milestone 1 — Android launcher foundation

- [ ] Create an Android project with Kotlin and Jetpack Compose.
- [ ] Add navigation: Home, Game Profile, Runtime, Input, Settings, Diagnostics.
- [ ] Add a stable runtime adapter interface.
- [ ] Add profile persistence with Room/DataStore.
- [ ] Add SAF-based file selection and a safe import/staging workflow.
- [ ] Add game-file validation without bundling copyrighted files.
- [ ] Add structured logging and error states.

### Acceptance criteria

- The app installs and starts on the target Android device.
- UI remains responsive during file validation/import.
- Invalid or incomplete game directories produce actionable errors.
- No Windows runtime is started from UI composables directly.

## Milestone 2 — Runtime integration

- [ ] Package or install the proven runtime components with pinned versions.
- [ ] Implement runtime manifest and SHA-256 verification.
- [ ] Create, validate, and back up Wine prefixes.
- [ ] Embed or present the game display surface.
- [ ] Implement process-tree monitoring and clean shutdown.
- [ ] Persist one runtime profile per tested configuration.

### Acceptance criteria

- The launcher can install/repair the runtime and launch the proven test target.
- Interrupted installation does not leave a profile marked as valid.
- A crashed session does not corrupt the game directory or next startup.

## Milestone 3 — SA-MP PC workflow

- [ ] Detect the original SA-MP PC client files.
- [ ] Validate the selected GTA directory and client revision.
- [ ] Implement the verified client launch sequence.
- [ ] Add editable nickname and favorite server list.
- [ ] Add clear connection and startup diagnostics.
- [ ] Test on more than one compatible server implementation where legally and technically available.

### Acceptance criteria

- The original SA-MP client is used; no native replacement client is involved.
- Server connection, basic movement, chat input, and clean exit pass smoke tests.
- Client and runtime versions are included in diagnostics.

## Milestone 4 — Input and usability

- [ ] Create editable touchscreen layouts.
- [ ] Support keyboard and standard gamepad mapping.
- [ ] Support adjustable button size, opacity, position, and visibility.
- [ ] Add safe-area and aspect-ratio handling.
- [ ] Add pause/session controls without stealing game input unexpectedly.

### Acceptance criteria

- All essential game controls can be reached on a phone display.
- Controls do not cover critical UI by default and can be moved by the user.
- Touch input remains stable through orientation and app lifecycle changes.

## Milestone 5 — Performance and device profiles

- [ ] Build a repeatable benchmark scene and test procedure.
- [ ] Collect frame times, memory, startup duration, thermal state, and crash rate.
- [ ] Compare graphics and CPU translation candidates.
- [ ] Add tested profiles for Mali and Adreno families separately.
- [ ] Add conservative automatic profile selection.
- [ ] Add adaptive resolution only after measuring visual and input effects.

### Acceptance criteria

- Every profile is backed by test data and device metadata.
- Automatic selection never chooses a configuration marked incompatible.
- The launcher does not claim unsupported FPS targets.
- Thermal adaptation respects Android's thermal APIs and never attempts to disable system protections.

## Milestone 6 — Release readiness

- [ ] Select and document the repository's source license.
- [ ] Complete third-party license and notice audit.
- [ ] Add reproducible build instructions and CI checks.
- [ ] Add crash-report privacy controls.
- [ ] Test clean install, upgrade, runtime repair, and data backup.
- [ ] Publish a compatibility table with explicit tested/not-tested labels.

### Release gate

No public release should imply broad SA-MP PC compatibility until the exact runtime/client/device combinations have passed the documented test matrix.
