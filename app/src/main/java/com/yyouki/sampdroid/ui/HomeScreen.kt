package com.yyouki.sampdroid.ui

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.yyouki.sampdroid.device.DeviceProbe
import com.yyouki.sampdroid.device.formatDiagnosticReport
import com.yyouki.sampdroid.device.calculateAppCpuCores
import com.yyouki.sampdroid.device.DeviceReport
import com.yyouki.sampdroid.device.DeviceSnapshot
import com.yyouki.sampdroid.device.ThermalState
import com.yyouki.sampdroid.device.formatMemoryMb
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.text.DateFormat
import java.util.Date

private const val SAMPLE_INTERVAL_MS = 5_000L

@Composable
fun HomeScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var device by remember(context) {
        mutableStateOf(DeviceProbe.readDeviceReport(context))
    }
    var snapshot by remember(context) {
        mutableStateOf(DeviceProbe.readSnapshot(context))
    }
    var appCpuCores by remember(context) { mutableStateOf<Double?>(null) }
    var previousCpuSample by remember(context) {
        mutableStateOf(DeviceProbe.readCpuTimeSample())
    }
    var isForeground by remember(lifecycleOwner) {
        mutableStateOf(
            lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED),
        )
    }

    // Observe the screen lifecycle so periodic sampling stops in the background.
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> isForeground = true
                Lifecycle.Event.ON_STOP -> isForeground = false
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Keep telemetry sampling lightweight and foreground-only.
    LaunchedEffect(isForeground) {
        if (!isForeground) return@LaunchedEffect

        // Start a fresh interval after resume; never include time spent in the background.
        previousCpuSample = DeviceProbe.readCpuTimeSample()
        appCpuCores = null

        while (isForeground && isActive) {
            // Wait before sampling so the CPU metric represents a complete interval.
            delay(SAMPLE_INTERVAL_MS)
            if (!isForeground || !isActive) break

            // Device identity rarely changes; only dynamic telemetry is sampled periodically.
            snapshot = DeviceProbe.readSnapshot(context)

            val currentCpuSample = DeviceProbe.readCpuTimeSample()
            appCpuCores = calculateAppCpuCores(previousCpuSample, currentCpuSample)
            previousCpuSample = currentCpuSample
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Header(
                isForeground = isForeground,
                sampledAt = snapshot.sampledAtEpochMs,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
            )

            DeviceCard(report = device)

            MemoryCard(snapshot = snapshot)

            SystemHealthCard(
                snapshot = snapshot,
                appCpuCores = appCpuCores,
            )

            DiagnosticNote()

            OutlinedButton(
                onClick = {
                    device = DeviceProbe.readDeviceReport(context)
                    snapshot = DeviceProbe.readSnapshot(context)
                    previousCpuSample = DeviceProbe.readCpuTimeSample()
                    appCpuCores = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            ) {
                Text("REFRESH NOW", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = {
                    val reportText = formatDiagnosticReport(device, snapshot, appCpuCores)
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "Android device diagnostic report")
                        putExtra(Intent.EXTRA_TEXT, reportText)
                    }
                    // The user selects the destination in Android's Sharesheet.
                    context.startActivity(
                        Intent.createChooser(sendIntent, "Share diagnostic report"),
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            ) {
                Text("SHARE DIAGNOSTIC REPORT", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "The report contains device details and current measurements. Review it before sharing.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun Header(
    isForeground: Boolean,
    sampledAt: Long,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "DEVICE LAB",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
            )
            Text(
                text = "SYSTEM PERFORMANCE",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp,
                letterSpacing = 1.5.sp,
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            StatusChip(
                label = if (isForeground) "LIVE" else "PAUSED",
                color = if (isForeground) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
            TextButton(
                onClick = onToggleTheme,
            ) {
                Text(
                    text = if (isDarkTheme) "LIGHT MODE" else "DARK MODE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Text(
                text = DateFormat.getTimeInstance(DateFormat.SHORT)
                    .format(Date(sampledAt)),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 5.dp),
            )
        }
    }
}

@Composable
private fun DeviceCard(report: DeviceReport) {
    Panel(title = "DEVICE INFORMATION") {
        Text(
            text = report.displayName.ifBlank { "Unknown device" },
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
        )
        MetricRow("Manufacturer", report.manufacturer.ifBlank { "Unavailable" })
        MetricRow("SoC", report.socModel.ifBlank { "Unavailable" })
        MetricRow("Hardware", report.hardware.ifBlank { "Unavailable" })
        MetricRow("CPU threads", report.cpuThreadCount.toString())
        MetricRow(
            "Android",
            "${report.androidRelease} · API ${report.apiLevel}",
        )
        MetricRow(
            "Supported ABIs",
            report.supportedAbis.joinToString().ifBlank { "Unavailable" },
        )
    }
}

@Composable
private fun MemoryCard(snapshot: DeviceSnapshot) {
    val usedPercent = snapshot.usedMemoryPercent
    Panel(title = "MEMORY") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Column {
                Text(
                    text = formatMemoryMb(snapshot.usedMemoryMb),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "System memory in use",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp,
                )
            }
            Text(
                text = usedPercent?.let { "$it%" } ?: "N/A",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        LinearProgressIndicator(
            progress = { (usedPercent ?: 0) / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        MetricRow("Total RAM", formatMemoryMb(snapshot.totalMemoryMb))
        MetricRow("Available RAM", formatMemoryMb(snapshot.availableMemoryMb))
        MetricRow("App memory (PSS)", formatMemoryMb(snapshot.appPssMb))
    }
}

@Composable
private fun SystemHealthCard(
    snapshot: DeviceSnapshot,
    appCpuCores: Double?,
) {
    val thermalColor = when (snapshot.thermalState) {
        ThermalState.NONE, ThermalState.LIGHT -> MaterialTheme.colorScheme.secondary
        ThermalState.MODERATE -> MaterialTheme.colorScheme.tertiary
        ThermalState.UNKNOWN -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.error
    }

    Panel(title = "SYSTEM HEALTH") {
        MetricRow(
            "Battery",
            snapshot.batteryPercent?.let { "$it%" } ?: "Unavailable",
        )
        MetricRow("Battery state", snapshot.batteryState.displayName)
        MetricRow("Power source", snapshot.batterySource)
        MetricRow(
            "App CPU",
            appCpuCores?.let { String.format(java.util.Locale.US, "%.2f cores", it) }
                ?: "Sampling...",
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Thermal status",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
            )
            StatusChip(
                label = snapshot.thermalState.displayName.uppercase(),
                color = thermalColor,
            )
        }
        MetricRow(
            "Power saver",
            snapshot.powerSaveMode?.let { if (it) "On" else "Off" } ?: "Unavailable",
        )
        MetricRow(
            "Low-memory warning",
            snapshot.lowMemory?.let { if (it) "Active" else "No" } ?: "Unavailable",
        )
    }
}

@Composable
private fun DiagnosticNote() {
    Panel(title = "SAMPLING") {
        Text(
            text = "Telemetry refreshes every 5 seconds while this screen is visible. Sampling pauses when the app moves to the background.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            lineHeight = 18.sp,
        )
        Text(
            text = "GPU model, driver version, and system-wide CPU utilization are not guessed. App CPU is shown as core-equivalents for this process only.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            lineHeight = 18.sp,
        )
    }
}

@Composable
private fun Panel(
    title: String,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.4.sp,
            )
            content()
        }
    }
}

@Composable
private fun StatusChip(
    label: String,
    color: androidx.compose.ui.graphics.Color,
) {
    Surface(
        color = color.copy(alpha = 0.13f),
        shape = RoundedCornerShape(50),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            color = color,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
        )
    }
}

@Composable
private fun MetricRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
        )
        Text(
            text = value,
            modifier = Modifier
                .weight(1f)
                .padding(start = 14.dp),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.End,
        )
    }
}
