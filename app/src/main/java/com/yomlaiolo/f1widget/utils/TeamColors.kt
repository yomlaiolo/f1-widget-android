package com.yomlaiolo.f1widget.utils

import androidx.compose.ui.graphics.Color

object TeamColors {
    
    fun getColor(constructorId: String): Color {
        return when (constructorId.lowercase()) {
            "red_bull" -> Color(0xFF1E41FF)
            "ferrari" -> Color(0xFFDC0000)
            "mercedes" -> Color(0xFF00D2BE)
            "mclaren" -> Color(0xFFFF8700)
            "aston_martin" -> Color(0xFF006F62)
            "alpine" -> Color(0xFF0090FF)
            "williams" -> Color(0xFF005AFF)
            "alphatauri", "rb" -> Color(0xFF2B4562)
            "alfa", "sauber", "kick_sauber" -> Color(0xFF900000)
            "haas" -> Color(0xFFFFFFFF)
            else -> Color(0xFF999999)
        }
    }
}