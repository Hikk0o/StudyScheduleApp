package com.hikko.scheduleapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.hikko.scheduleapp.ActivityUtils.activitiesIsLoaded
import com.hikko.scheduleapp.ActivityUtils.getActivitiesDayOfWeek
import com.hikko.scheduleapp.ActivityUtils.getIdByDay
import com.hikko.scheduleapp.ActivityUtils.loadAllActivities
import com.hikko.scheduleapp.ActivityUtils.localeDayOfWeek
import com.hikko.scheduleapp.MainActivity.Companion.getActiveDayOfWeek
import com.hikko.scheduleapp.MainActivity.Companion.setActiveDayOfWeek
import com.hikko.scheduleapp.MainActivity.Companion.setActiveDayOfWeekId
import com.hikko.scheduleapp.adapters.WidgetService

/**
 * Implementation of App Widget functionality.
 */
class ActivitiesDayWidget : AppWidgetProvider() {
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == OPEN_APP) {
            setActiveDayOfWeek(localeDayOfWeek)
            setActiveDayOfWeekId(getIdByDay(getActiveDayOfWeek()))
            val mainActivityIntent = Intent(context, MainActivity::class.java)
            mainActivityIntent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(mainActivityIntent)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        val views = RemoteViews(context.packageName, R.layout.activities_day_widget)
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
            val arrayList: List<Activity>? = getActivitiesDayOfWeek(localeDayOfWeek)
            if (arrayList != null) {
                if (arrayList.isEmpty()) {
                    views.setViewVisibility(R.id.no_activities_text_widget, View.VISIBLE)
                } else {
                    views.setViewVisibility(R.id.no_activities_text_widget, View.GONE)
                }
            }
            views.setOnClickPendingIntent(R.id.clickable_layout, getPendingIntent(context))
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun getPendingIntent(context: Context): PendingIntent {
            val widgetIntent = Intent(context, ActivitiesDayWidget::class.java)
            widgetIntent.action = OPEN_APP
            //        Intent intent = new Intent(context, MainActivity.class);
            return PendingIntent.getBroadcast(
                context,
                0,
                widgetIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            //        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }

        @JvmStatic
        fun updateWidget(context: Context) {
            val ids = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(ComponentName(context, ActivitiesDayWidget::class.java))
            val intent = Intent(context, ActivitiesDayWidget::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            val appWidgetManager = AppWidgetManager.getInstance(context)
            appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.ActivitiesListView_widget)
            context.sendBroadcast(intent)
        }
    }
}