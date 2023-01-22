package com.hikko.scheduleapp.widgetMain.adapters

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.LightingColorFilter
import android.os.Build
import android.view.View
import android.widget.LinearLayout
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.hikko.scheduleapp.Activity
import com.hikko.scheduleapp.ActivityUtils.getActivitiesDayOfWeek
import com.hikko.scheduleapp.ActivityUtils.localeDayOfWeek
import com.hikko.scheduleapp.R
import com.hikko.scheduleapp.Settings
import com.hikko.scheduleapp.pageEditActivities.adapters.WidgetIntent

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
            activities = getActivitiesDayOfWeek(localeDayOfWeek)
            return activities!!.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            val views = RemoteViews(mContext.packageName, R.layout.widget_activities_day_item)
            val v = View.inflate(mContext, R.layout.widget_activities_day_item, null)

            views.setInt(R.id.widget_background, "setBackgroundResource", R.drawable.day_of_week_round_corner_active)

//            views.setImageViewResource(R.id.widget_background, R.drawable.day_of_week_round_corner_active)
//            val drawable = AppCompatResources.getDrawable(
//                mContext,
//                R.drawable.activity_round_corner
//            )
//            drawable?.colorFilter = LightingColorFilter(Color.parseColor("#FF000000"), Settings.config.themeColor)
//            views.setImageViewBitmap(R.id.widget_background, drawable?.toBitmap(1,1,null))
//            views.setRemoteAdapter(R.id.widget_background, Intent(mContext, WidgetIntent::class.java))

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
//            Log.i(TAG, views.toString())
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