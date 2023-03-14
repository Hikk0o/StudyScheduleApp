package com.hikko.scheduleapp.pages.pageMain.adapters

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.hikko.scheduleapp.ActivityUtils.localeDay
import com.hikko.scheduleapp.R
import com.hikko.scheduleapp.utilClasses.DayOfEpoch
import java.time.LocalDate

class DaysListAdapter(
    private val mContext: Context,
    private val resourceLayout: Int,
    private val mData: List<DayOfEpoch>,
    private val res: Resources
    ) : RecyclerView.Adapter<DaysListAdapter.ViewHolder>() {

    private var activeDayId: Int = localeDay
    private lateinit var activeDayView: View

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val month: TextView = itemView.findViewById(R.id.month_name)
        val dayOfMonth: TextView = itemView.findViewById(R.id.day_of_month)
        val dayOfWeek: TextView = itemView.findViewById(R.id.day_of_week)
        val dayButton: ConstraintLayout = itemView.findViewById(R.id.day)
        val res: Resources = itemView.resources
        val activeDrawable = ResourcesCompat.getDrawable(res, R.drawable.day_of_week_round_corner_active, null)
        val inactiveDrawable = ResourcesCompat.getDrawable(res, R.drawable.day_of_week_round_corner, null)}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = mContext
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val itemView = inflater.inflate(resourceLayout, parent, false)
        // Return a new holder instance
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: DaysListAdapter.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)

        val numberDay = mData[position].numberDay

        if (numberDay == activeDayId) {
            holder.dayButton.background = holder.activeDrawable
            activeDayView = holder.dayButton
        }


        val epoch = LocalDate.of(1970, 1, 1)
        val date = epoch.plusDays(numberDay.toLong())
        val monthNames = arrayOf(
            "Январь",
            "Февраль",
            "Март",
            "Апрель",
            "Май",
            "Июнь",
            "Июль",
            "Август",
            "Сентябрь",
            "Октябрь",
            "Ноябрь",
            "Декабрь"
        )
        val month = holder.month
        month.text = monthNames[date.month.value - 1]

        val dayOfMonth = holder.dayOfMonth
        dayOfMonth.text = date.dayOfMonth.toString()


        val button = holder.dayButton
        button.setOnClickListener {
            val intent = Intent("CHANGE_CURRENT_DAY")
            intent.putExtra("dayId", numberDay)
            intent.putExtra("dayIndex", position)
            mContext.sendBroadcast(intent)
            activeDayView.background = holder.inactiveDrawable
            holder.dayButton.background = holder.activeDrawable

            activeDayView = holder.dayButton
            activeDayId = numberDay

//        val horizontalScrollView = findViewById<HorizontalScrollView>(R.id.horizontalScrollView)
//        horizontalScrollView.post { horizontalScrollView.smoothScrollTo(v.x.toInt() - 300, 0) }

        }
        val dayOfWeek = holder.dayOfWeek
        dayOfWeek.text = getStrDayOfWeek(date.dayOfWeek.value).uppercase()
    }

    private fun getStrDayOfWeek(id: Int): String {
        return when (id) {
            1 -> res.getString(R.string.day1)
            2 -> res.getString(R.string.day2)
            3 -> res.getString(R.string.day3)
            4 -> res.getString(R.string.day4)
            5 -> res.getString(R.string.day5)
            6 -> res.getString(R.string.day6)
            7 -> res.getString(R.string.day7)
            else -> ""
        }
    }
}