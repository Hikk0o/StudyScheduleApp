package com.hikko.scheduleapp.pageMain.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.hikko.scheduleapp.Activity
import com.hikko.scheduleapp.R

class ActivitiesListAdapter(
    private val mContext: Context,
    private val resourceLayout: Int,
    data: List<Activity>,
    res: Resources
) : ArrayAdapter<Activity?>(
    mContext, resourceLayout, data
) {
    init {
        for (activity in data) {
            val name = activity.name
            if (name.isEmpty()) {
                activity.name = res.getString(R.string.empty_name_activity)
                activity.type = ""
            }
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val vi: LayoutInflater = LayoutInflater.from(mContext)
            view = vi.inflate(resourceLayout, null)
        }
        val activity = getItem(position)

        // Тип пары
        val activityType = view!!.findViewById<TextView>(R.id.activity_type)
        activityType.text = activity!!.type

        // Кабинет
        val activityCabinet = view.findViewById<TextView>(R.id.activity_cabinet)
        if (activity.cabinet.isNotEmpty()) {
            activityCabinet.visibility = View.VISIBLE
            activityCabinet.text = activity.cabinet
        } else {
            activityCabinet.visibility = View.GONE
        }

        // Название пары
        val activityName = view.findViewById<TextView>(R.id.activity_name)
        activityName.text = activity.name

        // Время начала пары
        val activityStart = view.findViewById<TextView>(R.id.activity_start)
        activityStart.text = activity.startTime

        // Время конца пары
        val activityEnd = view.findViewById<TextView>(R.id.activity_end)
        activityEnd.text = activity.endTime
        return view
    }
}