package com.hikko.scheduleapp;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utils {

    public static void saveWeekToJsonFile(File filesDir) {

        File week = new File(filesDir, "week.json");

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        List<List<Activity>> activitiesOfWeek = new ArrayList<>();

        if (loadedActivities != null) {
            activitiesOfWeek = loadedActivities;
        }

        if (activitiesOfWeek.size() < 7) {
            while (activitiesOfWeek.size() < 7) {
                activitiesOfWeek.add(new ArrayList<>());
            }
        }
        String json = gson.toJson(activitiesOfWeek);

        // Create file output stream
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(week);
            // Write a line to the file
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
            // Close the file output stream
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<List<Activity>> loadedActivities = null;

    public static void loadAllActivities(File filesDir) {

        Gson g = new Gson();
        File file = new File(filesDir, "week.json");
        if (!file.exists()) {
            saveWeekToJsonFile(filesDir);
            loadAllActivities(filesDir);
        } else {
            try {
                byte[] content = Files.readAllBytes(file.toPath());
                String str = new String(content, StandardCharsets.UTF_8);
                Type arrType = new TypeToken<List<List<Activity>>>(){}.getType();
                loadedActivities = new ArrayList<>();
                loadedActivities = g.fromJson(str, arrType);
                if (loadedActivities.size() == 0) {
                    saveWeekToJsonFile(filesDir);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static List<List<Activity>> getLoadedActivities() {
        return loadedActivities;
    }

    public static void setLoadedActivities(List<List<Activity>> editedActivities) {
        loadedActivities = editedActivities;
    }

    public static ArrayList<HashMap<String, String>> getActivitiesDayOfWeek(int dayOfWeek) {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        HashMap<String, String> map;

        dayOfWeek -= 1;

        if (loadedActivities == null) {
            return new ArrayList<>();
        }

        List<List<Activity>> activity = loadedActivities;

        if (dayOfWeek > activity.size()-1) {
            return new ArrayList<>();
        }

        for (Activity value : activity.get(dayOfWeek)) {
            map = new HashMap<>();
            map.put("Name", value.name);
            map.put("Start",  value.start_time);
            map.put("End", value.end_time);
            map.put("Type", value.type);
            arrayList.add(map);
        }

        return arrayList;
    }

    public static boolean clearInputFocus(View v, Context context) {
        v.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        return false;
    }

    public static int getIdByDay(int id) {
        if (id == 1) {
            return R.id.day1;
        } else if (id == 2) {
            return R.id.day2;
        } else if (id == 3) {
            return R.id.day3;
        } else if (id == 4) {
            return R.id.day4;
        } else if (id == 5) {
            return R.id.day5;
        } else if (id == 6) {
            return R.id.day6;
        } else if (id == 7) {
            return R.id.day7;
        }
        return id;
    }

    public static int getDayById(int id) {
        if (id == R.id.day1) {
            return 1;
        } else if (id == R.id.day2) {
            return 2;
        } else if (id == R.id.day3) {
            return 3;
        } else if (id == R.id.day4) {
            return 4;
        } else if (id == R.id.day5) {
            return 5;
        } else if (id == R.id.day6) {
            return 6;
        } else if (id == R.id.day7) {
            return 7;
        }
        return id;
    }

    public static int getLocaleDayOfWeek() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        return dayOfWeek.getValue();
    }


}

