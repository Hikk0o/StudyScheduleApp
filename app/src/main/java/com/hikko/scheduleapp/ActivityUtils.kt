package com.hikko.scheduleapp

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.hikko.scheduleapp.utilClasses.DayOfEpoch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object ActivityUtils {
    private const val TAG = "ActivityUtils"
    private var savedFilesDir: File? = null

    @JvmStatic
    fun saveWeekToJsonFile(filesDir: File) {
        Log.i(TAG, "Save week to Json file...")
        savedFilesDir = filesDir
        val week = File(filesDir, "week.json")
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()
        val savedDaysOfEpoch: ArrayList<DayOfEpoch> = loadedDays

        if (savedDaysOfEpoch.size < 30) {

            var dayEpoch: Int = if (savedDaysOfEpoch.isEmpty()) {
                localeDay
            } else {
                savedDaysOfEpoch.last().numberDay + 1
            }

            while (savedDaysOfEpoch.size < 30) {
                savedDaysOfEpoch.add(DayOfEpoch(dayEpoch))
                dayEpoch++
            }
        }

        val json = gson.toJson(savedDaysOfEpoch)

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

    private var loadedDays: ArrayList<DayOfEpoch> = ArrayList()

    @JvmStatic
    fun loadAllActivities(filesDir: File) {
        savedFilesDir = filesDir
        Log.i(TAG, "Load all activities...")
        val g = Gson()
        val file = File(filesDir, "week.json")
        if (!file.exists()) {
            saveWeekToJsonFile(filesDir)
            loadAllActivities(filesDir)
        } else {
            try {
                val content = Files.readAllBytes(file.toPath())
                val str = String(content, StandardCharsets.UTF_8)
                val arrType = object : TypeToken<ArrayList<DayOfEpoch>>() {}.type

                loadedDays = g.fromJson(str, arrType)
                if (loadedDays.size == 0) {
                    saveWeekToJsonFile(filesDir)
                    loadAllActivities(filesDir)
                    return
                }
                if (loadedDays.last().numberDay - localeDay < 30) {

                    var dayEpoch: Int = loadedDays.last().numberDay + 1

                    while (loadedDays.last().numberDay - localeDay < 30) {
                        loadedDays.add(DayOfEpoch(dayEpoch))
                        dayEpoch++
                    }
                }
            } catch (e: IOException) {
                loadedDays = ArrayList()
                e.printStackTrace()
            } catch (e: JsonSyntaxException) {
                file.delete()
                saveWeekToJsonFile(filesDir)
                loadAllActivities(filesDir)
            }
        }
    }

    @JvmStatic
    fun getLoadedDays(): ArrayList<DayOfEpoch> {
        return loadedDays
    }

    @JvmStatic
    fun setLoadedDays(editedDays: ArrayList<DayOfEpoch>) {
        loadedDays = editedDays
    }

    @JvmStatic
    fun getDayOfEpoch(dayOfEpoch: Int): DayOfEpoch {
        val loadedDays: ArrayList<DayOfEpoch> = this.loadedDays
        val filteredDays = loadedDays.filter { it.numberDay == dayOfEpoch }

        return if (filteredDays.isEmpty()) {
            DayOfEpoch(-1)
        } else {
            filteredDays[0]
        }
    }

    @JvmStatic
    fun clearInputFocus(v: View, context: Context): Boolean {
        v.clearFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
        return false
    }

    @JvmStatic
    val localeDay: Int
        get() {
            val date = LocalDate.now()
            val epoch = LocalDate.of(1970, 1, 1)
            return ChronoUnit.DAYS.between(epoch, date).toInt()
        }

    @JvmStatic
    val activitiesIsLoaded: Boolean
        get() {
            return true
        }


}