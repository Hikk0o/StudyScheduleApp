package com.hikko.scheduleapp

import android.app.Application
import android.util.Log

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i("MainApplication", "Init application...")
        Settings.loadConfigFromStorage(filesDir)
        ActivityUtils.loadAllActivities(filesDir)
        ActivitiesDayWidget.updateWidget(applicationContext)
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.i("MainApplication", "Bye!")
    }
}