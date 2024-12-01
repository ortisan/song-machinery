package com.ortisan.songmachinery.tuner.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ortisan.songmachinery.audio.Frequency.Companion.frequencyToPitch

@Composable
fun Tuner(frequency: Double) {
    val animatedValue = remember { Animatable(0f) }

    LaunchedEffect(frequency) {
        animatedValue.animateTo(frequency.toFloat(), animationSpec = tween(durationMillis = 500))
    }

    val pitch = frequencyToPitch(frequency)

    Text(
        text = "${pitch} (${String.format("%.2f", frequency)} Hz)",
        fontSize = 24.sp
    )

    Canvas(
        modifier = Modifier
            .size(300.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val radius = size.minDimension / 2.2f
        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2
        val startAngle = 180f
        val sweepAngle = 180f

        drawArc(
            color = Color.LightGray,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = androidx.compose.ui.geometry.Offset(
                x = centerX - radius,
                y = centerY - radius
            ),
            size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
            style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
        )

        val tickCount = 10
        for (i in 0..tickCount) {
            val angle = startAngle + (i * (sweepAngle / tickCount))
            val angleRadians = Math.toRadians(angle.toDouble())
            val startX = centerX + (radius - 20.dp.toPx()) * kotlin.math.cos(angleRadians).toFloat()
            val startY = centerY + (radius - 20.dp.toPx()) * kotlin.math.sin(angleRadians).toFloat()
            val endX = centerX + radius * kotlin.math.cos(angleRadians).toFloat()
            val endY = centerY + radius * kotlin.math.sin(angleRadians).toFloat()

            drawLine(
                color = Color.Black,
                start = androidx.compose.ui.geometry.Offset(startX, startY),
                end = androidx.compose.ui.geometry.Offset(endX, endY),
                strokeWidth = 4f
            )
        }

        val needleAngle = startAngle + (animatedValue.value / 100 * sweepAngle)
        val needleRadians = Math.toRadians(needleAngle.toDouble())
        val needleEndX = centerX + (radius - 40.dp.toPx()) * kotlin.math.cos(needleRadians).toFloat()
        val needleEndY = centerY + (radius - 40.dp.toPx()) * kotlin.math.sin(needleRadians).toFloat()

        drawLine(
            color = Color.Red,
            start = androidx.compose.ui.geometry.Offset(centerX, centerY),
            end = androidx.compose.ui.geometry.Offset(needleEndX, needleEndY),
            strokeWidth = 8f,
            cap = StrokeCap.Round
        )

        drawCircle(
            color = Color.Black,
            radius = 10.dp.toPx(),
            center = androidx.compose.ui.geometry.Offset(centerX, centerY)
        )
    }
}