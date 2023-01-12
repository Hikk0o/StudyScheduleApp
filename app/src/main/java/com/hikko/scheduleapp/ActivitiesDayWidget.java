package com.hikko.scheduleapp;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.hikko.scheduleapp.adapters.WidgetService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implementation of App Widget functionality.
 */
public class ActivitiesDayWidget extends AppWidgetProvider {
    private static final String TAG = "ActivitiesDayWidget";

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.i(TAG, "Widget update");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activities_day_widget);

        Intent serviceIntent = new Intent(context, WidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setRemoteAdapter(R.id.ActivitiesListView_widget, serviceIntent);

        ArrayList<HashMap<String, String>> arrayList;
        if (!Utils.activitiesIsLoaded()) {
            Utils.loadAllActivities(context.getFilesDir());
        }

        arrayList = Utils.getActivitiesDayOfWeek(Utils.getLocaleDayOfWeek());
        if (arrayList != null) {
            if (arrayList.size() == 0) {
                views.setViewVisibility(R.id.no_activities_text_widget, View.VISIBLE);
            } else {
                views.setViewVisibility(R.id.no_activities_text_widget, View.GONE);
            }
        }
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.clickable_layout, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateWidget(Context context) {
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, ActivitiesDayWidget.class));

        Intent intent = new Intent(context, ActivitiesDayWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.ActivitiesListView_widget);

        context.sendBroadcast(intent);

    }

}