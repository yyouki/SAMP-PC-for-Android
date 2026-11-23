# Architecture Decision Records

This file records decisions and current assumptions. Update it when test evidence changes the direction.

## ADR-0001 — Preserve the original SA-MP PC client

**Status:** Accepted

**Decision:** The product must run the original Windows SA-MP PC client with a user-supplied compatible GTA: San Andreas PC installation. A native Android multiplayer reimplementation is outside the project scope.

**Reasoning:** This is the defining requirement of the project and determines the runtime, architecture, and compatibility tests.

**Consequence:** The project must solve 32-bit Windows x86 execution on Android ARM64, Wine compatibility, game rendering, input, and the original client's startup behavior.

## ADR-0002 — Separate launcher UI from runtime implementation

**Status:** Accepted

**Decision:** Keep Android UI and game runtime behind a small runtime adapter API.

**Reasoning:** The runtime is the highest-risk component and may need to be replaced as experiments progress. UI code should not depend directly on Wine commands, filesystem internals, or a specific translator.

**Consequence:** Runtime operations must return typed results and structured diagnostics. No shell commands are assembled in Compose UI.

## ADR-0003 — Use Zalith Launcher as a UX/architecture reference, not as the game runtime

**Status:** Accepted

**Decision:** Study Zalith Launcher for Android navigation, profiles, runtime management, settings, and input customization. Do not use PojavLauncher as the SA-MP execution engine.

**Reasoning:** Zalith targets Minecraft Java. This project targets Windows x86 game binaries and requires a different compatibility stack.

**Consequence:** Reuse design patterns where appropriate, but independently implement the Windows runtime integration. Any source-code reuse must first pass a license and attribution review.

## ADR-0004 — Runtime execution path remains undecided until proof

**Status:** Open

**Decision:** Do not lock Box86, Box64/WoW64, Box32, or another path as the production solution until the full target chain has passed tests.

**Reasoning:** CPU translation, 32-bit Windows support, Android userspace, Wine build, graphics, and SA-MP initialization interact. Support statements for one component do not prove the combined stack works.

**Exit criteria:** One documented candidate passes the CPU, Wine GUI, Direct3D 9, GTA, SA-MP startup, server-join, and clean-relaunch tests.

## ADR-0005 — Device-specific graphics profiles

**Status:** Accepted

**Decision:** Graphics presets are selected from tested device/driver profiles, with a conservative fallback. Do not assume one renderer works on both Mali and Adreno.

**Reasoning:** GPU vendors and Android drivers expose different APIs and capabilities. Turnip is not a Mali driver.

**Consequence:** Renderer selection is capability-checked and logged. Experimental options are hidden behind an advanced setting and never silently enabled on an untested device.

## ADR-0006 — Benchmark evidence required for performance claims

**Status:** Accepted

**Decision:** No FPS, memory, startup, or compatibility claim is published without a reproducible test record.

**Reasoning:** Best-case FPS screenshots can hide frame pacing, thermal throttling, crashes, or incompatible sessions.

**Consequence:** Each optimization changes one variable at a time and reruns the compatibility smoke tests.

## ADR-0007 — Game files are user supplied

**Status:** Accepted

**Decision:** The launcher may validate and manage a user's own game installation but must not bundle or redistribute GTA: San Andreas data or proprietary SA-MP installers without explicit redistribution rights.

**Reasoning:** Game assets and installers have separate copyright and distribution terms.

**Consequence:** Provide a clear import workflow, compatibility checks, and documentation that points users to official requirements without mirroring copyrighted files.

## ADR-0008 — Repository license is not selected yet

**Status:** Open

**Decision:** Do not add a repository-wide LICENSE file until the owner chooses a license after reviewing planned dependencies and any source-code reuse.

**Reasoning:** The project may contain original code plus third-party components with different licenses. A premature license choice can create avoidable obligations.

**Exit criteria:** Audit planned dependencies, decide whether any GPL-licensed code will be reused or modified, and document notices and source-distribution obligations.
