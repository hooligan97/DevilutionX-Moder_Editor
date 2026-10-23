package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BloodRed
import com.example.ui.theme.BloodRedBright
import com.example.ui.theme.BloodRedDark
import com.example.ui.theme.ArcaneBlue
import com.example.ui.theme.ArcaneBlueBright
import com.example.ui.theme.ArcaneBlueDark

@Composable
fun DiabloOrb(
    title: String,
    currentValue: Int,
    maxValue: Int,
    isLife: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val fraction = if (maxValue > 0) (currentValue.toFloat() / maxValue).coerceIn(0f, 1f) else 1f
    val baseGrad = if (isLife) {
        listOf(BloodRedBright, BloodRed, BloodRedDark)
    } else {
        listOf(ArcaneBlueBright, ArcaneBlue, ArcaneBlueDark)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(92.dp)
                .clip(CircleShape)
                .background(Color(0xFF0F0B09))
                .clickable { onClick() }
                .testTag(if (isLife) "life_orb" else "mana_orb"),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val diameter = size.minDimension
                val fillHeight = diameter * fraction
                val topY = diameter - fillHeight

                // Draw filled liquid portion
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = baseGrad,
                        startY = topY,
                        endY = diameter
                    ),
                    topLeft = Offset(0f, topY),
                    size = Size(diameter, fillHeight)
                )

                // Glass orb rim reflection
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color.Transparent, Color(0x66FFFFFF), Color(0x99000000)),
                        center = Offset(diameter * 0.35f, diameter * 0.35f),
                        radius = diameter * 0.6f
                    ),
                    radius = diameter / 2f
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$currentValue",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "/ $maxValue",
                    color = Color(0xCCFFFFFF),
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            color = if (isLife) BloodRedBright else ArcaneBlueBright,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
