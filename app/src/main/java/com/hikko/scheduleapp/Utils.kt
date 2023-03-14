package com.hikko.scheduleapp

import android.graphics.Color
import android.graphics.LightingColorFilter
import android.view.View


object Utils {
    fun updateThemeColor(view: View, colorDarken: Boolean) {
        val color: Int = if (colorDarken) {
            colorDarken(Color.valueOf(Settings.config.themeColor))
        } else {
            Settings.config.themeColor
        }

        if (Settings.config.isCustomThemeColor) {
            view.background.colorFilter = LightingColorFilter(Color.parseColor("#FF000000"), color)
        } else {
            view.background.clearColorFilter()
        }
        view.invalidate()
    }

    private const val colorFraction = 0.27f
    private fun colorDarken(color: Color): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color.toArgb(), hsv)
        if (hsv[2] > colorFraction) {
            hsv[2] -= colorFraction
        } else {
            hsv[2] += colorFraction
        }
        return Color.valueOf(Color.HSVToColor(hsv)).toArgb()
    }
}