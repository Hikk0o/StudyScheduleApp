package com.hikko.scheduleapp

import android.content.DialogInterface
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.hikko.scheduleapp.ActivityUtils.activitiesIsLoaded
import com.hikko.scheduleapp.ActivityUtils.getActivitiesDayOfWeek
import com.hikko.scheduleapp.ActivityUtils.getDayById
import com.hikko.scheduleapp.ActivityUtils.getIdByDay
import com.hikko.scheduleapp.ActivityUtils.loadAllActivities
import com.hikko.scheduleapp.ActivityUtils.localeDayOfWeek
import com.hikko.scheduleapp.adapters.ActivitiesListAdapter
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class MainActivity : AppCompatActivity() {
    private var drawableDayOfWeek: Drawable? = null
    private var drawableDayOfWeekActive: Drawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<View>(activeDayOfWeekId)

        val button = findViewById<View>(R.id.editActivitiesButton)
        button.setOnClickListener {
            val intent = Intent(this, EditActivitiesOfDay::class.java)
            startActivity(intent)
        }

        drawableDayOfWeek = AppCompatResources.getDrawable(applicationContext, R.drawable.day_of_week_round_corner)
        drawableDayOfWeekActive = AppCompatResources.getDrawable(applicationContext, R.drawable.day_of_week_round_corner_active)

        // Color Theme
        updateThemeColor()

        changeCurrentWeek(view)

        findViewById<ImageView>(R.id.buttonRgbSelector).setOnClickListener {
            showColorPickerDialog()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }

    fun changeCurrentWeek(v: View) {
        findViewById<View>(activeDayOfWeekId).background = drawableDayOfWeek
        v.background = drawableDayOfWeekActive

        activeDayOfWeekId = v.id
        activeDayOfWeek = getDayById(activeDayOfWeekId)
        val horizontalScrollView = findViewById<HorizontalScrollView>(R.id.horizontalScrollView)
        horizontalScrollView.post { horizontalScrollView.smoothScrollTo(v.x.toInt() - 300, 0) }
        if (!activitiesIsLoaded) {
            loadAllActivities(filesDir)
        }
        val arrayList: List<Activity>? = getActivitiesDayOfWeek(activeDayOfWeek)
        val noActivitiesText = findViewById<TextView>(R.id.no_activities_text)
        if (arrayList != null) {
            if (arrayList.isEmpty()) {
                noActivitiesText.visibility = View.VISIBLE
            } else {
                noActivitiesText.visibility = View.GONE
            }
        }
        val activitiesListView = findViewById<ListView>(R.id.ActivitiesListView)
        val adapter = ActivitiesListAdapter(
            this, R.layout.day_item, arrayList!!,
            this.resources
        )
        activitiesListView.adapter = adapter
        activitiesListView.divider = null
        activitiesListView.isVerticalScrollBarEnabled = false
    }

    companion object {
        private var activeDayOfWeek = localeDayOfWeek
        private var activeDayOfWeekId = getIdByDay(activeDayOfWeek)
        @JvmStatic
        fun getActiveDayOfWeek(): Int {
            return activeDayOfWeek
        }

        @JvmStatic
        fun setActiveDayOfWeek(dayOfWeek: Int) {
            activeDayOfWeek = dayOfWeek
        }

        @JvmStatic
        fun setActiveDayOfWeekId(dayOfWeekId: Int) {
            activeDayOfWeekId = dayOfWeekId
        }
    }

    private fun showColorPickerDialog() {
        ColorPickerDialog.Builder(findViewById<View>(R.id.mainLayout).context)
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton(("Confirm"), ColorEnvelopeListener { colorEnvelope: ColorEnvelope, _: Boolean ->
                run {
                    Settings.config.setThemeColor(Color.valueOf(colorEnvelope.color), true)
                    Settings.config.isCustomThemeColor = true
                    updateThemeColor()
                }
            })
            .setNegativeButton(("Сбросить")) { _: DialogInterface, _: Int ->
                run {
                    Settings.config.isCustomThemeColor = false
                    updateThemeColor()
                }
            }
            .attachAlphaSlideBar(false)
            .attachBrightnessSlideBar(true)
            .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
            .show()
    }

    private fun updateThemeColor() {
        if (Settings.config.isCustomThemeColor && drawableDayOfWeek != null && drawableDayOfWeekActive != null) {
            val mainColor = Settings.config.themeColor
            val blackMainColor = Utils.colorBrighten(Color.valueOf(mainColor), Utils.colorFraction).toArgb()
            drawableDayOfWeek!!.colorFilter = LightingColorFilter(Color.parseColor("#FF000000"), blackMainColor)
            drawableDayOfWeekActive!!.colorFilter = LightingColorFilter(Color.parseColor("#FF000000"), mainColor)


        } else {
            drawableDayOfWeek = AppCompatResources.getDrawable(applicationContext, R.drawable.day_of_week_round_corner)
            drawableDayOfWeekActive = AppCompatResources.getDrawable(applicationContext, R.drawable.day_of_week_round_corner_active)
        }
        val idsOfDaysLayouts: IntArray = ActivityUtils.getIdsDaysOfWeek()
        for (id in idsOfDaysLayouts) {
            findViewById<View>(id).background = drawableDayOfWeek
            findViewById<View>(id).invalidate()
        }

        findViewById<View>(activeDayOfWeekId).background = drawableDayOfWeekActive
        findViewById<View>(activeDayOfWeekId).invalidate()
    }
}
