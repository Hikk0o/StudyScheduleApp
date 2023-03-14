package com.hikko.scheduleapp.pages.widgetMain.adapters

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.graphics.toColor
import com.hikko.scheduleapp.utilClasses.Activity
import com.hikko.scheduleapp.ActivityUtils.getDayOfEpoch
import com.hikko.scheduleapp.ActivityUtils.localeDay
import com.hikko.scheduleapp.PageActivity
import com.hikko.scheduleapp.R
import com.hikko.scheduleapp.Settings

class WidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return WidgetViewsFactory(applicationContext, intent)
    }

    internal class WidgetViewsFactory(private val mContext: Context, intent: Intent) :
        RemoteViewsFactory {
        private val appWidgetId: Int
        private var activities: List<Activity>? = null

        init {
            appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        override fun onCreate() {}
        override fun onDataSetChanged() {}
        override fun onDestroy() {}
        override fun getCount(): Int {
            activities = getDayOfEpoch(localeDay).activitiesList
            return activities!!.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            val views = RemoteViews(mContext.packageName, R.layout.widget_activities_day_item)
            val v = View.inflate(mContext, R.layout.widget_activities_day_item, null)

            if (Settings.config.isCustomThemeColor) {
                views.setInt(R.id.main_widget_background, "setColorFilter", PageActivity.colorDarken(Settings.config.themeColor.toColor()))
            }

            val name = activities!![position].name
            val cabinet = activities!![position].cabinet
            views.setTextViewText(R.id.activity_start, activities!![position].startTime)
            views.setTextViewText(R.id.activity_end, activities!![position].endTime)
            if (name.isEmpty()) {
                views.setTextViewText(
                    R.id.activity_name,
                    v.resources.getText(R.string.empty_name_activity)
                )
                views.setTextViewText(R.id.activity_type, "")
            } else {
                views.setTextViewText(R.id.activity_name, name)
                views.setTextViewText(R.id.activity_type, activities!![position].type)
                if (cabinet.isEmpty()) {
                    views.setViewVisibility(R.id.activity_cabinet, View.GONE)
                } else {
                    views.setTextViewText(R.id.activity_cabinet, cabinet)
                    views.setViewVisibility(R.id.activity_cabinet, View.VISIBLE)
                }
            }
            return views
        }

        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }
    }
}