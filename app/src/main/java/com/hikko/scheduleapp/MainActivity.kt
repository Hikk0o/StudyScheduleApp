package com.hikko.scheduleapp

import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.HorizontalScrollView
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
        if (drawableDayOfWeek != null && drawableDayOfWeekActive != null) {
            drawableDayOfWeek!!.colorFilter = LightingColorFilter(Color.parseColor("#FF000000"), Settings.config.themeColor)
            drawableDayOfWeekActive!!.colorFilter = LightingColorFilter(Color.parseColor("#FF000000") + 0x7777333, Settings.config.themeColor)
            val idsOfDaysLayouts: IntArray = ActivityUtils.getIdsDaysOfWeek()
            for (id in idsOfDaysLayouts) {
                findViewById<View>(id).background = drawableDayOfWeek
            }
        }
        changeCurrentWeek(view)
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
}
