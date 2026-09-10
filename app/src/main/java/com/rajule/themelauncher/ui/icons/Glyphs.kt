package com.rajule.themelauncher.ui.icons

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import com.rajule.themelauncher.model.GlyphId
import kotlin.math.cos
import kotlin.math.sin

// Original generic line-icon set (no brand logos), drawn with Canvas primitives.
@Composable
fun Glyph(id: GlyphId, tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = w * 0.09f, cap = StrokeCap.Round)
        val c = Offset(w / 2f, h / 2f)

        when (id) {
            GlyphId.PHONE -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(w * 0.30f, h * 0.12f),
                    size = Size(w * 0.40f, h * 0.76f),
                    cornerRadius = CornerRadius(w * 0.10f),
                    style = stroke
                )
                drawLine(tint, Offset(w * 0.42f, h * 0.76f), Offset(w * 0.58f, h * 0.76f), strokeWidth = w * 0.07f, cap = StrokeCap.Round)
            }
            GlyphId.MESSAGE, GlyphId.CHAT -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(w * 0.14f, h * 0.20f),
                    size = Size(w * 0.72f, h * 0.50f),
                    cornerRadius = CornerRadius(w * 0.16f),
                    style = stroke
                )
                drawLine(tint, Offset(w * 0.30f, h * 0.70f), Offset(w * 0.30f, h * 0.86f), strokeWidth = w * 0.07f, cap = StrokeCap.Round)
                drawLine(tint, Offset(w * 0.30f, h * 0.86f), Offset(w * 0.46f, h * 0.70f), strokeWidth = w * 0.07f, cap = StrokeCap.Round)
            }
            GlyphId.CAMERA -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(w * 0.14f, h * 0.30f),
                    size = Size(w * 0.72f, h * 0.50f),
                    cornerRadius = CornerRadius(w * 0.08f),
                    style = stroke
                )
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(w * 0.38f, h * 0.18f),
                    size = Size(w * 0.24f, h * 0.14f),
                    cornerRadius = CornerRadius(w * 0.03f),
                    style = stroke
                )
                drawCircle(tint, radius = w * 0.14f, center = c, style = stroke)
            }
            GlyphId.IMAGE -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(w * 0.14f, h * 0.18f),
                    size = Size(w * 0.72f, h * 0.64f),
                    cornerRadius = CornerRadius(w * 0.08f),
                    style = stroke
                )
                drawCircle(tint, radius = w * 0.07f, center = Offset(w * 0.34f, h * 0.38f), style = stroke)
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(w * 0.22f, h * 0.72f)
                    lineTo(w * 0.42f, h * 0.50f)
                    lineTo(w * 0.58f, h * 0.64f)
                    lineTo(w * 0.72f, h * 0.46f)
                    lineTo(w * 0.82f, h * 0.60f)
                }
                drawPath(path, tint, style = stroke)
            }
            GlyphId.BROWSER -> {
                drawCircle(tint, radius = w * 0.36f, center = c, style = stroke)
                drawLine(tint, Offset(w * 0.14f, h * 0.5f), Offset(w * 0.86f, h * 0.5f), strokeWidth = w * 0.06f)
                val ovalStroke = Stroke(width = w * 0.06f)
                drawOval(tint, topLeft = Offset(w * 0.36f, h * 0.14f), size = Size(w * 0.28f, w * 0.72f), style = ovalStroke)
            }
            GlyphId.SETTINGS -> {
                drawCircle(tint, radius = w * 0.16f, center = c, style = stroke)
                for (i in 0 until 8) {
                    val angle = Math.toRadians((i * 45).toDouble())
                    val inner = Offset(c.x + (cos(angle) * w * 0.28f).toFloat(), c.y + (sin(angle) * w * 0.28f).toFloat())
                    val outer = Offset(c.x + (cos(angle) * w * 0.40f).toFloat(), c.y + (sin(angle) * w * 0.40f).toFloat())
                    drawLine(tint, inner, outer, strokeWidth = w * 0.08f, cap = StrokeCap.Round)
                }
            }
            GlyphId.CLOCK -> {
                drawCircle(tint, radius = w * 0.36f, center = c, style = stroke)
                drawLine(tint, c, Offset(c.x, c.y - w * 0.20f), strokeWidth = w * 0.07f, cap = StrokeCap.Round)
                drawLine(tint, c, Offset(c.x + w * 0.14f, c.y), strokeWidth = w * 0.07f, cap = StrokeCap.Round)
            }
            GlyphId.CALCULATOR -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(w * 0.24f, h * 0.12f),
                    size = Size(w * 0.52f, h * 0.76f),
                    cornerRadius = CornerRadius(w * 0.08f),
                    style = stroke
                )
                drawLine(tint, Offset(w * 0.32f, h * 0.30f), Offset(w * 0.68f, h * 0.30f), strokeWidth = w * 0.06f, cap = StrokeCap.Round)
                val dotRadius = w * 0.035f
                for (row in 0..2) {
                    for (col in 0..1) {
                        drawCircle(
                            tint,
                            radius = dotRadius,
                            center = Offset(w * (0.40f + col * 0.20f), h * (0.48f + row * 0.15f))
                        )
                    }
                }
            }
            GlyphId.MAIL -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(w * 0.12f, h * 0.24f),
                    size = Size(w * 0.76f, h * 0.52f),
                    cornerRadius = CornerRadius(w * 0.06f),
                    style = stroke
                )
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(w * 0.14f, h * 0.28f)
                    lineTo(w * 0.5f, h * 0.56f)
                    lineTo(w * 0.86f, h * 0.28f)
                }
                drawPath(path, tint, style = stroke)
            }
            GlyphId.AT -> {
                drawCircle(tint, radius = w * 0.14f, center = c, style = stroke)
                drawArc(
                    color = tint,
                    startAngle = -90f,
                    sweepAngle = 260f,
                    useCenter = false,
                    topLeft = Offset(w * 0.14f, h * 0.14f),
                    size = Size(w * 0.72f, h * 0.72f),
                    style = stroke
                )
            }
            GlyphId.MUSIC -> {
                drawCircle(tint, radius = w * 0.11f, center = Offset(w * 0.30f, h * 0.74f), style = stroke)
                drawCircle(tint, radius = w * 0.11f, center = Offset(w * 0.66f, h * 0.64f), style = stroke)
                drawLine(tint, Offset(w * 0.41f, h * 0.74f), Offset(w * 0.41f, h * 0.24f), strokeWidth = w * 0.06f, cap = StrokeCap.Round)
                drawLine(tint, Offset(w * 0.77f, h * 0.64f), Offset(w * 0.77f, h * 0.20f), strokeWidth = w * 0.06f, cap = StrokeCap.Round)
                drawLine(tint, Offset(w * 0.41f, h * 0.24f), Offset(w * 0.77f, h * 0.20f), strokeWidth = w * 0.06f, cap = StrokeCap.Round)
            }
            GlyphId.PLAY -> {
                drawCircle(tint, radius = w * 0.36f, center = c, style = stroke)
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(w * 0.42f, h * 0.34f)
                    lineTo(w * 0.66f, h * 0.5f)
                    lineTo(w * 0.42f, h * 0.66f)
                    close()
                }
                drawPath(path, tint)
            }
            GlyphId.PIN -> {
                drawCircle(tint, radius = w * 0.22f, center = Offset(w * 0.5f, h * 0.38f), style = stroke)
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(w * 0.34f, h * 0.48f)
                    lineTo(w * 0.5f, h * 0.86f)
                    lineTo(w * 0.66f, h * 0.48f)
                }
                drawPath(path, tint, style = stroke)
            }
            GlyphId.NOTE -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(w * 0.20f, h * 0.12f),
                    size = Size(w * 0.60f, h * 0.76f),
                    cornerRadius = CornerRadius(w * 0.06f),
                    style = stroke
                )
                for (i in 0..2) {
                    drawLine(
                        tint,
                        Offset(w * 0.32f, h * (0.34f + i * 0.16f)),
                        Offset(w * 0.68f, h * (0.34f + i * 0.16f)),
                        strokeWidth = w * 0.05f,
                        cap = StrokeCap.Round
                    )
                }
            }
            GlyphId.STORE -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(w * 0.16f, h * 0.30f),
                    size = Size(w * 0.68f, h * 0.56f),
                    cornerRadius = CornerRadius(w * 0.06f),
                    style = stroke
                )
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(w * 0.30f, h * 0.30f)
                    quadraticBezierTo(w * 0.30f, h * 0.14f, w * 0.5f, h * 0.14f)
                    quadraticBezierTo(w * 0.70f, h * 0.14f, w * 0.70f, h * 0.30f)
                }
                drawPath(path, tint, style = stroke)
            }
            GlyphId.CLOUD -> {
                val path = androidx.compose.ui.graphics.Path().apply {
                    addOval(androidx.compose.ui.geometry.Rect(Offset(w * 0.16f, h * 0.38f), Size(w * 0.34f, w * 0.34f)))
                    addOval(androidx.compose.ui.geometry.Rect(Offset(w * 0.40f, h * 0.26f), Size(w * 0.40f, w * 0.40f)))
                    addOval(androidx.compose.ui.geometry.Rect(Offset(w * 0.56f, h * 0.40f), Size(w * 0.30f, w * 0.30f)))
                }
                drawPath(path, tint, style = stroke)
            }
            GlyphId.LOCK -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(w * 0.24f, h * 0.46f),
                    size = Size(w * 0.52f, h * 0.42f),
                    cornerRadius = CornerRadius(w * 0.08f),
                    style = stroke
                )
                drawArc(
                    color = tint,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(w * 0.32f, h * 0.18f),
                    size = Size(w * 0.36f, w * 0.36f),
                    style = stroke
                )
            }
            GlyphId.GRID -> {
                for (row in 0..1) {
                    for (col in 0..1) {
                        drawRoundRect(
                            color = tint,
                            topLeft = Offset(w * (0.18f + col * 0.38f), h * (0.18f + row * 0.38f)),
                            size = Size(w * 0.30f, w * 0.30f),
                            cornerRadius = CornerRadius(w * 0.05f)
                        )
                    }
                }
            }
            GlyphId.BATTERY -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(w * 0.14f, h * 0.32f),
                    size = Size(w * 0.62f, h * 0.36f),
                    cornerRadius = CornerRadius(w * 0.05f),
                    style = stroke
                )
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(w * 0.78f, h * 0.42f),
                    size = Size(w * 0.08f, h * 0.16f),
                    cornerRadius = CornerRadius(w * 0.02f)
                )
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(w * 0.20f, h * 0.38f),
                    size = Size(w * 0.32f, h * 0.24f),
                    cornerRadius = CornerRadius(w * 0.03f)
                )
            }
            GlyphId.MOON -> {
                val path = androidx.compose.ui.graphics.Path().apply {
                    addOval(androidx.compose.ui.geometry.Rect(Offset(w * 0.16f, h * 0.16f), Size(w * 0.68f, w * 0.68f)))
                }
                clipPath(path) {
                    drawOval(tint, topLeft = Offset(w * 0.30f, h * 0.10f), size = Size(w * 0.68f, w * 0.68f))
                }
            }
        }
    }
}
