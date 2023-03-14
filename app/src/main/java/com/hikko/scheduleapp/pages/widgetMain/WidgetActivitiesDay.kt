package com.hikko.scheduleapp.pages.widgetMain

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.hikko.scheduleapp.utilClasses.Activity
import com.hikko.scheduleapp.ActivityUtils.activitiesIsLoaded
import com.hikko.scheduleapp.ActivityUtils.getDayOfEpoch
import com.hikko.scheduleapp.ActivityUtils.loadAllActivities
import com.hikko.scheduleapp.ActivityUtils.localeDay
import com.hikko.scheduleapp.R
import com.hikko.scheduleapp.pages.pageMain.MainActivity
import com.hikko.scheduleapp.pages.widgetMain.adapters.WidgetService

/**
 * Implementation of App Widget functionality.
 */
class WidgetActivitiesDay : AppWidgetProvider() {
//    override fun onReceive(context: Context, intent: Intent) {
//        super.onReceive(context, intent)
//        println(intent.action)
//        if (intent.action == OPEN_APP) {
//            val mainActivityIntent = Intent(context, MainActivity::class.java)
//            mainActivityIntent.flags =
//                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            context.startActivity(mainActivityIntent)
//        }
//    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        val views = RemoteViews(context.packageName, R.layout.widget_activities)
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, views)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {
        private const val TAG = "ActivitiesDayWidget"
        private const val OPEN_APP = "OpenAppFromWidget"
        private fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int, views: RemoteViews
        ) {
            Log.i(TAG, "Widget update")
            val serviceIntent = Intent(context, WidgetService::class.java)
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            views.setRemoteAdapter(R.id.ActivitiesListView_widget, serviceIntent)
            if (!activitiesIsLoaded) {
                loadAllActivities(context.filesDir)
            }
            val arrayList: List<Activity> = getDayOfEpoch(localeDay).activitiesList
            if (arrayList.isEmpty()) {
                views.setViewVisibility(R.id.no_activities_text_widget, View.VISIBLE)
            } else {
                views.setViewVisibility(R.id.no_activities_text_widget, View.GONE)
            }
            views.setOnClickPendingIntent(R.id.clickable_layout, openMainActivity(context))
            views.setOnClickPendingIntent(R.id.widget_background, openMainActivity(context))

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun openMainActivity(context: Context): PendingIntent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("FROM_WIDGET", true)
            return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        @JvmStatic
        fun updateWidget(context: Context) {
            val ids = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(ComponentName(context, WidgetActivitiesDay::class.java))
            val intent = Intent(context, WidgetActivitiesDay::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            val appWidgetManager = AppWidgetManager.getInstance(context)
            appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.ActivitiesListView_widget)
            context.sendBroadcast(intent)
        }
    }
}