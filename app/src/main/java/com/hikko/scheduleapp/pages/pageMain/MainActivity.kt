package com.hikko.scheduleapp.pages.pageMain

import android.content.*
import android.graphics.*
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hikko.scheduleapp.*
import com.hikko.scheduleapp.ActivityUtils.activitiesIsLoaded
import com.hikko.scheduleapp.ActivityUtils.getDayOfEpoch
import com.hikko.scheduleapp.ActivityUtils.getLoadedDays
import com.hikko.scheduleapp.ActivityUtils.loadAllActivities
import com.hikko.scheduleapp.ActivityUtils.localeDay
import com.hikko.scheduleapp.pages.pageEditActivities.EditActivitiesOfDay
import com.hikko.scheduleapp.pages.pageMain.adapters.ActivitiesListAdapter
import com.hikko.scheduleapp.pages.pageMain.adapters.DaysListAdapter
import com.hikko.scheduleapp.utilClasses.Activity
import com.hikko.scheduleapp.utilClasses.DayOfEpoch
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.flag.BubbleFlag
import com.skydoves.colorpickerview.flag.FlagMode
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener


class MainActivity : PageActivity() {

    companion object {
        private var activeDay = localeDay
        private var TAG = "MainActivity"
        private var activeDayId = 0

        private var loadedDays = getLoadedDays()
        @JvmStatic
        fun getActiveDay(): Int {
            return activeDay
        }

        @JvmStatic
        fun setActiveDay(dayOfWeek: Int) {
            activeDay = dayOfWeek
        }

        @JvmStatic
        fun setActiveDayOfWeekId(dayOfWeekId: Int) {
            activeDayId = dayOfWeekId
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val button = findViewById<View>(R.id.editActivitiesButton)
        button.setOnClickListener {
            val intent = Intent(this, EditActivitiesOfDay::class.java)
            startActivity(intent)
        }

        println(activeDay)

        // Color Theme
//        updateColors()
        // Set current day of week
        changeCurrentDay(activeDay, null)

        val daysList: List<DayOfEpoch> = loadedDays
        val daysListView = findViewById<RecyclerView>(R.id.DaysListView)
        daysListView.setHasFixedSize(true)
        val adapterDaysList = DaysListAdapter(
            this, R.layout.main_activity_day_of_month, daysList, this.resources
        )
        daysListView.adapter = adapterDaysList
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        daysListView.layoutManager = linearLayoutManager

        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null) {
                    val dayId: Int = intent.getIntExtra("dayId", 0)
                    val dayIndex: Int = intent.getIntExtra("dayIndex", 0)
                    changeCurrentDay(dayId, dayIndex)
                }
            }
        }, IntentFilter("CHANGE_CURRENT_DAY"))

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

    fun changeCurrentDay(dayId: Int, dayIndex: Int?) {
        activeDay = dayId

        val daysListView = findViewById<RecyclerView>(R.id.DaysListView)
        val index =
            dayIndex ?: (loadedDays.indexOf(loadedDays.filter { it.numberDay == dayId }[0]) or 0)

        // Smooth scroll to item
        val offset = 230
        val layoutManager = daysListView.layoutManager as LinearLayoutManager
        val finalScrollPosition =
            layoutManager.findViewByPosition(index)?.left ?: 0
        daysListView.smoothScrollBy(finalScrollPosition - offset, 0)

        if (!activitiesIsLoaded) {
            loadAllActivities(filesDir)
        }

        val activitiesList: List<Activity> = getDayOfEpoch(dayId)!!.activitiesList
        val noActivitiesText = findViewById<TextView>(R.id.no_activities_text)
        if (activitiesList.isEmpty()) {
            noActivitiesText.visibility = View.VISIBLE
        } else {
            noActivitiesText.visibility = View.GONE
        }
        val activitiesListView = findViewById<ListView>(R.id.ActivitiesListView)
        val adapterActivitiesList = ActivitiesListAdapter(
            this, R.layout.main_activity_day_item, activitiesList, this.resources
        )
        activitiesListView.adapter = adapterActivitiesList
        activitiesListView.divider = null
        activitiesListView.isVerticalScrollBarEnabled = false
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
             super.updateThemeColor(activeDayId, false)
         } else {
             resetColors()
         }
    }

    private fun resetColors() {
        val activeDay = findViewById<View>(activeDayId)
        activeDay.background = AppCompatResources.getDrawable(
            applicationContext,
            R.drawable.day_of_week_round_corner_active
        )

    }
}
