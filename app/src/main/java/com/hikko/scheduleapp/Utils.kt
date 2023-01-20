package com.hikko.scheduleapp

import android.graphics.Color


object Utils {
    fun colorBrighten(color: Color, fraction: Float): Color {
        val hsv = FloatArray(3)
        Color.colorToHSV(color.toArgb(), hsv)
        if (hsv[2] > fraction) {
            hsv[2] -= fraction
        } else {
            hsv[2] += fraction
        }
        return Color.valueOf(Color.HSVToColor(hsv))
    }

    val colorFraction = 0.27f
}