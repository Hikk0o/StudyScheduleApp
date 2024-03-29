package com.hikko.scheduleapp

import android.graphics.Color
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import kotlin.concurrent.thread

object Settings {
    private const val TAG = "AppSettings"
    private var savedFilesDir: File? = null
    var configLoaded: Boolean = false
    var config: Config = Config()

    fun loadConfigFromStorage(filesDir: File) {
        savedFilesDir = filesDir
        val configFile = File(savedFilesDir, "config.json")
        val gson = Gson()

        // Save config to storage if does not exists
        if (!configFile.exists()) {
            Log.i(TAG, "Creating and saving config in storage..")
            saveConfigToStorage()
        }
        // Read bytes from file and convert to String format
        val bytesContent = Files.readAllBytes(configFile.toPath())
        val content = String(bytesContent, StandardCharsets.UTF_8)

        // Convert string to class (Config)
        val arrType = object : TypeToken<Config>(){}.type
        val storageConfig: Config = gson.fromJson(content, arrType)

        config = storageConfig
//        config.themeColor = Color.parseColor("#8d20a8")
        configLoaded = true
        Log.i(TAG, "Config loaded")
    }

    private fun saveConfigToStorage() {
        thread {
            val configFile = File(savedFilesDir, "config.json")
            val gson = GsonBuilder()
                .setPrettyPrinting()
                .create()

            val outputStream = FileOutputStream(configFile)

            outputStream.write(gson.toJson(config).toByteArray(StandardCharsets.UTF_8))
            outputStream.close()
        }
    }

    class Config {
        var version: String = ""
        var themeColor: Int = 0
        get() {
            return 0x0 + field
        }

        var isCustomThemeColor: Boolean = false

        fun setThemeColor(color: Color) {
            themeColor = color.toArgb()
        }

        fun saveConfig() {
            saveConfigToStorage()
        }
    }
}