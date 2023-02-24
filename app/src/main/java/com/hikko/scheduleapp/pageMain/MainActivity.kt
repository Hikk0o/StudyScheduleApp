package com.hikko.scheduleapp.pageMain

import android.content.DialogInterface
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.hikko.scheduleapp.*
import com.hikko.scheduleapp.ActivityUtils.activitiesIsLoaded
import com.hikko.scheduleapp.ActivityUtils.getActivitiesDayOfWeek
import com.hikko.scheduleapp.ActivityUtils.getDayById
import com.hikko.scheduleapp.ActivityUtils.getIdByDay
import com.hikko.scheduleapp.ActivityUtils.loadAllActivities
import com.hikko.scheduleapp.ActivityUtils.localeDayOfWeek
import com.hikko.scheduleapp.pageEditActivities.EditActivitiesOfDay
import com.hikko.scheduleapp.pageMain.adapters.ActivitiesListAdapter
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.flag.BubbleFlag
import com.skydoves.colorpickerview.flag.FlagMode
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener


class MainActivity : PageActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val view = findViewById<View>(activeDayOfWeekId)

        val button = findViewById<View>(R.id.editActivitiesButton)
        button.setOnClickListener {
            val intent = Intent(this, EditActivitiesOfDay::class.java)
            startActivity(intent)
        }


        // Color Theme
        updateColors()

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
        val activeDrawable = findViewById<View>(activeDayOfWeekId).background
        findViewById<View>(activeDayOfWeekId).background = v.background
        v.background = activeDrawable

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
            this, R.layout.main_activity_day_item, arrayList!!,
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
        val bubbleFlag = BubbleFlag(this)
        bubbleFlag.flagMode = FlagMode.FADE
        val colorPickerDialog = ColorPickerDialog.Builder(findViewById<View>(R.id.mainLayout).context)
            .setPreferenceName("ColorPickerDialog")
            .setPositiveButton(applicationContext.getText(R.string.save), ColorEnvelopeListener { colorEnvelope: ColorEnvelope, _: Boolean ->
                run {
                    Settings.config.setThemeColor(Color.valueOf(colorEnvelope.color), true)
                    Settings.config.isCustomThemeColor = true
                    updateColors()
                }
            })
            .setNegativeButton(applicationContext.getText(R.string.reset)) { _: DialogInterface, _: Int ->
                run {
                    Settings.config.isCustomThemeColor = false
                    resetColors()
                }
            }
            .attachAlphaSlideBar(false)
            .attachBrightnessSlideBar(true)
            .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.

        colorPickerDialog.colorPickerView.flagView = bubbleFlag
        colorPickerDialog.show()

    }

    private fun updateColors() {
         if (Settings.config.isCustomThemeColor) {
             val idsOfDaysLayouts: IntArray = ActivityUtils.getIdsDaysOfWeek()
             super.updateThemeColor(idsOfDaysLayouts, true)
             super.updateThemeColor(activeDayOfWeekId, false)
         } else {
             resetColors()
         }
    }

    private fun resetColors() {
        val idsOfDaysLayouts: IntArray = ActivityUtils.getIdsDaysOfWeek()
        super.updateThemeColor(idsOfDaysLayouts, true)
        val activeDay = findViewById<View>(activeDayOfWeekId)
        activeDay.background = AppCompatResources.getDrawable(
            applicationContext,
            R.drawable.day_of_week_round_corner_active
        )

    }
}
