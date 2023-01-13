package com.hikko.scheduleapp

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.time.LocalDate
import java.util.ArrayList
import java.util.HashMap

object Utils {
    private const val TAG = "Utils"
    private var savedFilesDir: File? = null

    @JvmStatic
    fun saveWeekToJsonFile(filesDir: File) {
        Log.i(TAG, "saveWeekToJsonFile")
        savedFilesDir = filesDir
        val week = File(filesDir, "week.json")
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()
        var activitiesOfWeek: ArrayList<List<Activity>>? = ArrayList()
        if (loadedActivities != null) {
            activitiesOfWeek = loadedActivities
        }
        if (activitiesOfWeek!!.size < 7) {
            while (activitiesOfWeek.size < 7) {
                activitiesOfWeek.add(ArrayList())
            }
        }
        val json = gson.toJson(activitiesOfWeek)

        // Create file output stream
        val outputStream: FileOutputStream
        try {
            outputStream = FileOutputStream(week)
            // Write a line to the file
            outputStream.write(json.toByteArray(StandardCharsets.UTF_8))
            // Close the file output stream
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private var loadedActivities: ArrayList<List<Activity>>? = null

    @JvmStatic
    fun loadAllActivities(filesDir: File) {
        savedFilesDir = filesDir
        Log.i(TAG, "loadAllActivities")
        val g = Gson()
        val file = File(filesDir, "week.json")
        if (!file.exists()) {
            saveWeekToJsonFile(filesDir)
            loadAllActivities(filesDir)
        } else {
            try {
                val content = Files.readAllBytes(file.toPath())
                val str = String(content, StandardCharsets.UTF_8)
                val arrType = object : TypeToken<ArrayList<List<Activity?>?>?>() {}.type

                loadedActivities = g.fromJson(str, arrType)
                if (loadedActivities?.size == 0) {
                    saveWeekToJsonFile(filesDir)
                }
            } catch (e: IOException) {
                loadedActivities = ArrayList()
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun getLoadedActivities(): ArrayList<List<Activity>>? {
        return loadedActivities
    }

    @JvmStatic
    fun setLoadedActivities(editedActivities: ArrayList<List<Activity>>?) {
        loadedActivities = editedActivities
    }

    @JvmStatic
    fun getActivitiesDayOfWeek(day: Int): ArrayList<HashMap<String, String?>>? {
        var dayOfWeek = day
        val arrayList = ArrayList<HashMap<String, String?>>()
        var map: HashMap<String, String?>
        dayOfWeek -= 1
        if (loadedActivities == null) {
            if (savedFilesDir != null) {
                loadAllActivities(savedFilesDir!!)
            } else {
                Log.w(TAG, "loadedActivities is null")
                return null
            }
        }
        val activitiesOfWeek: List<List<Activity>>? = loadedActivities
        if (dayOfWeek > activitiesOfWeek!!.size - 1) {
            Log.w(TAG, "dayOfWeek is > activity.size() - 1")
            return ArrayList()
        }
        for (value in activitiesOfWeek[dayOfWeek]) {
            map = HashMap()
            map["Name"] = value.name
            map["Start"] = value.startTime
            map["End"] = value.endTime
            map["Type"] = value.type
            arrayList.add(map)
        }
        return arrayList
    }

    @JvmStatic
    fun clearInputFocus(v: View, context: Context): Boolean {
        v.clearFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
        return false
    }

    @JvmStatic
    fun getIdByDay(id: Int): Int {
        return when (id) {
            1 -> {
                R.id.day1
            }
            2 -> {
                R.id.day2
            }
            3 -> {
                R.id.day3
            }
            4 -> {
                R.id.day4
            }
            5 -> {
                R.id.day5
            }
            6 -> {
                R.id.day6
            }
            7 -> {
                R.id.day7
            }
            else -> id
        }
    }

    @JvmStatic
    fun getDayById(id: Int): Int {
        when (id) {
            R.id.day1 -> {
                return 1
            }
            R.id.day2 -> {
                return 2
            }
            R.id.day3 -> {
                return 3
            }
            R.id.day4 -> {
                return 4
            }
            R.id.day5 -> {
                return 5
            }
            R.id.day6 -> {
                return 6
            }
            R.id.day7 -> {
                return 7
            }
            else -> return id
        }
    }

    @JvmStatic
    val localeDayOfWeek: Int
        get() {
            val today = LocalDate.now()
            val dayOfWeek = today.dayOfWeek
            return dayOfWeek.value
        }

    @JvmStatic
    fun activitiesIsLoaded(): Boolean {
        return loadedActivities != null
    }
}