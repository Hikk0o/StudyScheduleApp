package com.hikko.scheduleapp

import android.app.Application
import android.util.Log
import com.hikko.scheduleapp.pages.widgetMain.WidgetActivitiesDay
import kotlin.concurrent.thread

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i("MainApplication", "Init application...")
        Settings.loadConfigFromStorage(filesDir)
        ActivityUtils.loadAllActivities(filesDir)
        thread {
            WidgetActivitiesDay.updateWidget(applicationContext, "MainApplication onCreate")
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.i("MainApplication", "Bye!")
    }
}