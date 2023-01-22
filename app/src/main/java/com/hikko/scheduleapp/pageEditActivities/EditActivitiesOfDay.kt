package com.hikko.scheduleapp.pageEditActivities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.hikko.scheduleapp.widgetMain.WidgetActivitiesDay.Companion.updateWidget
import com.hikko.scheduleapp.Activity
import com.hikko.scheduleapp.ActivityUtils.clearInputFocus
import com.hikko.scheduleapp.ActivityUtils.getActivitiesDayOfWeek
import com.hikko.scheduleapp.ActivityUtils.getLoadedActivities
import com.hikko.scheduleapp.ActivityUtils.saveWeekToJsonFile
import com.hikko.scheduleapp.ActivityUtils.setLoadedActivities
import com.hikko.scheduleapp.PageActivity
import com.hikko.scheduleapp.pageMain.MainActivity
import com.hikko.scheduleapp.pageMain.MainActivity.Companion.getActiveDayOfWeek
import com.hikko.scheduleapp.R
import com.hikko.scheduleapp.pageEditActivities.adapters.EditActivityAdapter

class EditActivitiesOfDay : PageActivity() {
    private var addActivityButton: View? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_activities)
        val activitiesListView = findViewById<ListView>(R.id.EditActivitiesListView)
        val tempList = getActivitiesDayOfWeek(getActiveDayOfWeek())
        activitiesOfDayList.clear()
        if (tempList != null) {
            for (activity in tempList) {
                activitiesOfDayList.add(activity.clone())
            }
        }
        updateActivitiesListView()

        // Add button to footer
        addActivityButton = View.inflate(
            this.layoutInflater.context,
            R.layout.edit_activities_button,
            null
        )
        activitiesListView.addFooterView(addActivityButton)
        findViewById<View>(R.id.backround).setOnTouchListener { _: View?, _: MotionEvent? ->
            clearInputFocus(
                activitiesListView,
                activitiesListView.context
            )
        }


        val cancelButton = findViewById<View>(R.id.cancel_button)
        cancelButton.setOnClickListener {
            goBackToMainActivity()
        }

        val saveButton = findViewById<View>(R.id.save_button)
        saveButton.setOnClickListener {
            saveActivitiesList()
        }

        val idLayouts: IntArray = intArrayOf(saveButton.id, cancelButton.id)
        super.updateThemeColor(idLayouts, false)

    }

    private fun goBackToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        goBackToMainActivity()
    }

    fun createNewActivity(view: View) {
        activitiesOfDayList.add(Activity())
        updateActivitiesListView()
        val maxActivities = view.resources.getInteger(R.integer.max_EditActivities)
        if (activitiesOfDayList.size >= maxActivities) {
            val activitiesListView = findViewById<ListView>(R.id.EditActivitiesListView)
            activitiesListView.removeFooterView(addActivityButton)
        }

        // Scrolling to new item
        val activitiesListView = findViewById<ListView>(R.id.EditActivitiesListView)
        activitiesListView.setSelection(activitiesOfDayList.size - 1)
    }

    private fun updateActivitiesListView() {
        val activitiesListView = findViewById<ListView>(R.id.EditActivitiesListView)
        val activities = ArrayList<Activity>()
        var count = 0
        for (activity in activitiesOfDayList) {
            activity.id = count++
            activities.add(activity)
        }
        val adapter = EditActivityAdapter(
            this, R.layout.edit_activities_item, activities
        )
        
        activitiesListView.adapter = adapter
        activitiesListView.divider = null
        activitiesListView.isVerticalScrollBarEnabled = false
    }

    private fun saveActivitiesList() {
        val editedActivitiesOfWeek: ArrayList<ArrayList<Activity>> = getLoadedActivities()
        val activities = ArrayList<Activity>()
        for (activity in activitiesOfDayList) {
            val start = activity.startTime
            val end = activity.endTime
            if (start.length < 4 || end.length < 4) {
                Toast.makeText(
                    this,
                    "Сначала заполните время для пар",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            activities.add(activity)
        }

        activities.sortBy { it.startTime }

        editedActivitiesOfWeek[getActiveDayOfWeek()-1] = activities
        setLoadedActivities(editedActivitiesOfWeek)

        Log.i(TAG, (editedActivitiesOfWeek[getActiveDayOfWeek()-1] == activities).toString())
        saveWeekToJsonFile(applicationContext.filesDir)
        updateWidget(applicationContext)
        goBackToMainActivity()
    }

    companion object {
        private const val TAG = "EditActivitiesOfDay"
        var activitiesOfDayList: MutableList<Activity> = ArrayList()
        fun deleteActivity(pos: Int) {
            activitiesOfDayList.removeAt(pos)
        }
    }
}