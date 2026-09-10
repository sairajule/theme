@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.rajule.themelauncher.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rajule.themelauncher.model.AppRole
import com.rajule.themelauncher.model.GlyphId
import com.rajule.themelauncher.model.ThemePalette
import com.rajule.themelauncher.ui.icons.Glyph
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TileSurface(
    palette: ThemePalette,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(palette.surface.copy(alpha = 0.62f))
            .then(
                if (onClick != null || onLongClick != null) {
                    Modifier.combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onClick?.invoke() },
                        onLongClick = { onLongClick?.invoke() }
                    )
                } else Modifier
            )
            .padding(10.dp),
        content = content
    )
}

@Composable
fun ClockTileView(now: LocalDateTime, palette: ThemePalette, modifier: Modifier = Modifier) {
    TileSurface(palette, modifier) {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Text(
                text = "%02d:%02d".format(now.hour, now.minute),
                color = palette.onSurface,
                fontSize = 44.sp,
                fontWeight = FontWeight.Light
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "${now.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())}, ${now.dayOfMonth} ${now.month.getDisplayName(TextStyle.FULL, Locale.getDefault())}",
                color = palette.onSurfaceMuted,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun CalendarTileView(now: LocalDateTime, palette: ThemePalette, modifier: Modifier = Modifier) {
    TileSurface(palette, modifier) {
        val yearMonth = YearMonth.from(now)
        val firstDay = yearMonth.atDay(1).dayOfWeek.value % 7
        val daysInMonth = yearMonth.lengthOfMonth()
        Column(Modifier.fillMaxSize()) {
            Text(
                text = now.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                color = palette.onSurface,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            Spacer(Modifier.height(6.dp))
            val totalCells = firstDay + daysInMonth
            val weeks = (totalCells + 6) / 7
            for (week in 0 until weeks) {
                Row(Modifier.fillMaxWidth()) {
                    for (dow in 0 until 7) {
                        val cellIndex = week * 7 + dow
                        val day = cellIndex - firstDay + 1
                        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            if (day in 1..daysInMonth) {
                                val isToday = day == now.dayOfMonth
                                Text(
                                    text = day.toString(),
                                    color = if (isToday) palette.background else palette.onSurfaceMuted,
                                    fontSize = 11.sp,
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                    modifier = if (isToday) {
                                        Modifier
                                            .clip(RoundedCornerShape(50))
                                            .background(palette.accent)
                                            .padding(horizontal = 5.dp, vertical = 1.dp)
                                    } else Modifier
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuoteTileView(text: String, palette: ThemePalette, modifier: Modifier = Modifier) {
    TileSurface(palette, modifier) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = palette.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 19.sp
            )
        }
    }
}

@Composable
fun BatteryTileView(percent: Int, palette: ThemePalette, modifier: Modifier = Modifier) {
    TileSurface(palette, modifier) {
        Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(50))
                    .background(palette.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Glyph(GlyphId.BATTERY, palette.accent, Modifier.size(18.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text("$percent%", color = palette.onSurface, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Text("Battery", color = palette.onSurfaceMuted, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun WeatherTileView(palette: ThemePalette, modifier: Modifier = Modifier) {
    TileSurface(palette, modifier) {
        Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Glyph(GlyphId.CLOUD, palette.accent, Modifier.size(26.dp))
            Spacer(Modifier.width(10.dp))
            Column {
                Text("--°", color = palette.onSurface, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("Offline", color = palette.onSurfaceMuted, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun PhotoTileView(seed: String, palette: ThemePalette, modifier: Modifier = Modifier) {
    val angle = (seed.hashCode() % 4 + 4) % 4
    val brush = when (angle) {
        0 -> Brush.linearGradient(listOf(palette.gradientTop, palette.gradientBottom))
        1 -> Brush.linearGradient(listOf(palette.gradientBottom, palette.gradientTop))
        2 -> Brush.verticalGradient(listOf(palette.surfaceVariant, palette.gradientTop))
        else -> Brush.verticalGradient(listOf(palette.gradientTop, palette.surfaceVariant))
    }
    Box(
        modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(brush)
    )
}

@Composable
fun AppShortcutTileView(
    role: AppRole,
    label: String,
    palette: ThemePalette,
    showLabel: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TileSurface(palette, modifier, onClick = onClick, onLongClick = onLongClick) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Glyph(role.glyph, palette.accent, Modifier.size(26.dp))
            if (showLabel) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = label,
                    color = palette.onSurfaceMuted,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}
