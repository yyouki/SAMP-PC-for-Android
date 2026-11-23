# Compatibility and Benchmark Plan

## Status vocabulary

Use only these labels:

- **Not tested** — no test has been run.
- **Blocked** — testing cannot proceed because a prerequisite is missing.
- **Failed** — a test was run and did not meet its acceptance criteria.
- **Experimental** — it works in a limited test but has known instability or incomplete coverage.
- **Passed** — all listed acceptance criteria passed under the recorded conditions.

Do not label a device or runtime combination "supported" based only on a successful game startup.

## Initial target device

| Field | Initial target |
|---|---|
| Device | Xiaomi Redmi 14C |
| SoC | MediaTek Helio G81-Ultra |
| GPU | ARM Mali-G52 MC2 |
| Android version/build | To be recorded from test device |
| RAM variant | To be confirmed (4/6/8 GB variants exist) |
| ABI | Must be confirmed at runtime |
| GPU driver version | To be recorded |
| Overall compatibility | Not tested |

These are target identifiers, not evidence that SA-MP PC currently works on this device.

## Test layers

| ID | Test | Pass condition |
|---|---|---|
| CPU-01 | Run 32-bit x86 Windows test program | Process exits with expected result |
| WINE-01 | Start basic Wine GUI program | Window renders and accepts input |
| GFX-01 | D3D9 capability test | No fatal renderer errors; output captured |
| GAME-01 | Start GTA: San Andreas PC | Main game renders and accepts input |
| SAMP-01 | Start original SA-MP PC client | Correct multiplayer startup, not single-player fallback |
| NET-01 | Connect to controlled server | Client joins and receives world/player state |
| INPUT-01 | Touch and keyboard input | Required inputs work without stuck/repeated keys |
| SESSION-01 | Clean shutdown and relaunch | Second launch works without manual prefix repair |
| THERMAL-01 | Sustained session | No app-induced system instability; thermal state is recorded |

## Runtime candidate matrix

| Candidate | Status | What must be verified |
|---|---|---|
| Box86 + 32-bit ARM userspace + 32-bit Wine | Not tested | Availability of compatible 32-bit userspace and execution support on target Android |
| Box64 + x86-64 Wine WoW64 | Not tested | WoW64 support, 32-bit process behavior, display and graphics integration |
| Box64 Box32 mode | Experimental candidate; not tested here | Build-time/runtime support, stability, performance and full client compatibility |
| ARM64 Wine WoW64 + Box64 support | Candidate; not tested here | Exact Wine build, required translation DLLs, process startup and graphics behavior |

The matrix is intentionally open. Do not pick a winner based on feature lists alone; run the same test set on each viable candidate.

## Graphics candidate matrix

| Path | Target use | Notes |
|---|---|---|
| WineD3D + tested Mali OpenGL path | Baseline candidate | Measure compatibility and CPU overhead on the exact driver |
| VirGL-based path | Mali candidate | Validate D3D9 behavior, frame pacing and overhead |
| Vulkan-based D3D9 translation | Conditional candidate | Only test if the device Vulkan driver and runtime expose the required capabilities |
| Turnip | Not a Mali candidate | Turnip targets Qualcomm Adreno; do not select it for Mali-G52 |
| Software rasterizer | Diagnostic only | Not a performance preset for real-time 3D gameplay |

Do not assume that a renderer supported by another Mali device is supported by the Redmi 14C. GPU model alone is not enough; record driver versions and actual capability results.

## Repeatable benchmark procedure

For each run, record:

1. Device model, Android build, ABI, RAM variant, SoC, GPU, and graphics driver.
2. Game executable version and file validation result.
3. SA-MP client revision and installer/source information.
4. Runtime manifest: Wine, CPU translator, userspace, graphics backend, and exact revisions.
5. Resolution, graphics settings, environment variables, and input profile.
6. Cold-start duration and warm-start duration.
7. Average FPS and frame-time distribution when measured reliably; record the measurement tool.
8. App/process memory (PSS/RSS where available).
9. Android thermal status at start and during the run.
10. Session duration, crashes, visual defects, input defects, and network test result.

Run the same scene or repeatable route at the same resolution and settings. Change only one configuration variable per comparison. Repeat runs and report median and spread, not only the best result.

Separate rendering benchmarks from network/server tests. Server load, latency, and packet loss can affect multiplayer experience without representing GPU performance.

## Suggested test sequence

1. Static capability inspection.
2. Small x86 Windows process.
3. Wine GUI.
4. D3D9 test.
5. GTA single-player launch and short scene test.
6. SA-MP client startup.
7. Controlled server join.
8. 20–30 minute sustained test, only after shorter tests pass.
9. Clean exit and cold relaunch.

The sustained-test duration is a test plan, not a claim that the device can maintain a specific FPS.

## Result template

Copy this block for every test run:

```yaml
test_id: ""
date_utc: ""
device_model: ""
android_build: ""
abi: ""
ram_variant_gb: null
soc: ""
gpu: ""
gpu_driver: ""
game_version: ""
samp_client_version: ""
runtime:
  wine: ""
  cpu_translator: ""
  userspace: ""
  graphics_backend: ""
  revision_or_commit: ""
settings:
  resolution: ""
  graphics_preset: ""
  environment: {}
results:
  status: "Not tested"
  startup_seconds: null
  average_fps: null
  frame_time_ms_p50: null
  frame_time_ms_p95: null
  memory_pss_mb: null
  thermal_start: ""
  thermal_end: ""
  session_minutes: null
  network_smoke_test: "Not tested"
  crashes: 0
  visual_or_input_issues: []
notes: ""
```
