package com.hikko.scheduleapp

import android.graphics.Color
import android.graphics.LightingColorFilter
import android.view.View
import androidx.appcompat.app.AppCompatActivity

abstract class PageActivity: AppCompatActivity() {
    private val colorFraction = 0.27f

    fun updateThemeColor(layouts: IntArray, colorDarken: Boolean) {
        println("AppActivity updateThemeColor")
        val color: Int = if (colorDarken) {
            colorDarken(Color.valueOf(Settings.config.themeColor))
        } else {
            Settings.config.themeColor
        }
        if (Settings.config.isCustomThemeColor) {

            for (layout in layouts) {
                val view = findViewById<View>(layout)
                view.background.colorFilter = LightingColorFilter(Color.parseColor("#FF000000"), color)
                view.invalidate()
            }
        } else {
            for (layout in layouts) {
                val view = findViewById<View>(layout)
                view.background.clearColorFilter()
                view.invalidate()
            }
        }
    }

    fun updateThemeColor(layout: Int, colorDarken: Boolean) {
        val color: Int = if (colorDarken) {
            colorDarken(Color.valueOf(Settings.config.themeColor))
        } else {
            Settings.config.themeColor
        }
        if (Settings.config.isCustomThemeColor) {
            val view = findViewById<View>(layout)
            view.background.colorFilter = LightingColorFilter(Color.parseColor("#FF000000"), color)
            view.invalidate()
        } else {
            val view = findViewById<View>(layout)
            view.background.clearColorFilter()
            view.invalidate()
        }
    }

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

