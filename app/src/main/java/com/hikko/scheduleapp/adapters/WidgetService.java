package com.hikko.scheduleapp.adapters;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.hikko.scheduleapp.R;
import com.hikko.scheduleapp.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetViewsFactory(getApplicationContext(), intent);
    }



    static class WidgetViewsFactory implements RemoteViewsFactory {

        private final Context mContext;
        private static final String TAG = "WidgetService";
        private int appWidgetId;
        private ArrayList<HashMap<String, String>> activities;

        WidgetViewsFactory(Context context, Intent intent) {
            mContext = context;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            activities = Utils.getActivitiesDayOfWeek(Utils.getLocaleDayOfWeek());
            Log.i(TAG, "activities " + activities);
            return activities.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_day_item);
            String name = activities.get(position).get("Name");
            views.setTextViewText(R.id.activity_start, activities.get(position).get("Start"));
            views.setTextViewText(R.id.activity_end, activities.get(position).get("End"));
            if (name == null || name.length() == 0) {
                View v = View.inflate(mContext, R.layout.activities_day_widget, null);
                views.setTextViewText(R.id.activity_name, v.getResources().getText(R.string.empty_name_activity));
                views.setTextViewText(R.id.activity_type, "");
            } else {
                views.setTextViewText(R.id.activity_name, name);
                views.setTextViewText(R.id.activity_type, activities.get(position).get("Type"));

            }
            Log.i(TAG, String.valueOf(views));
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
