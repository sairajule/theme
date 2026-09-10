package com.rajule.themelauncher.util

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import java.time.LocalDateTime

/** Ticks once a second so clock/calendar tiles stay live without any receiver plumbing in the UI. */
@Composable
fun rememberNow(): State<LocalDateTime> = produceState(initialValue = LocalDateTime.now()) {
    while (true) {
        value = LocalDateTime.now()
        delay(1000)
    }
}

/** Reads the real battery percentage via a sticky broadcast - no permission required. */
@Composable
fun rememberBatteryPercent(): State<Int> {
    val context = LocalContext.current
    return produceState(initialValue = readBattery(context)) {
        while (true) {
            value = readBattery(context)
            delay(30_000)
        }
    }
}

private fun readBattery(context: Context): Int {
    val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    val status: Intent? = context.registerReceiver(null, filter)
    val level = status?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale = status?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
    if (level < 0 || scale <= 0) return 0
    return (level * 100) / scale
}
