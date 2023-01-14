package com.hikko.scheduleapp

import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets

object Config {
    private const val TAG = "Config"
    private var savedFilesDir: File? = null

    @JvmStatic
    fun loadConfig(filesDir: File) {
        savedFilesDir = filesDir

        val configFile = File(savedFilesDir, "config.json")
        if (!configFile.exists()) {
            Log.i(TAG, "Create config..")
            val outputStream = FileOutputStream(configFile)
            outputStream.write("".toByteArray(StandardCharsets.UTF_8))
            outputStream.close()
        }
        Log.i(TAG, "Config loaded")
    }
}