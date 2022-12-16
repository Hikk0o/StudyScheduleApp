package com.hikko.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private final String[] activities = new String[] {null, "Методы анализа данных", "Теория алгоритмов и рекурсивных функций", "ТРПП", null};
    private FrameLayout frameLayout;

    private int activeDayOfWeekId = getIdByDay(getDayOfWeek());

    private int getDayOfWeek() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        return dayOfWeek.getValue();
    }


    private int getIdByDay(int id) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File internalStorageDir = getFilesDir();
        File week = new File(internalStorageDir, "week.json");
        // Create file output stream
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(week);
            // Write a line to the file
            fos.write("[{\"start\": \"11:11\",\"end\": \"12:12\",\"name\": \"Название пары\",\"type\": \"ПР\"}]".getBytes());
            // Close the file output stream
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(week.getAbsolutePath());

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        HashMap<String, String> map;

//        Gson g = new Gson();
//        File internalStorageDir = getFilesDir();
//        File file = new File(internalStorageDir, "week.json");
//        try {
//            byte[] content = Files.readAllBytes(file.toPath());
//            String str = new String(content, StandardCharsets.UTF_8);
//            Activity[] activity = g.fromJson(str, Activity[].class);
//            for (Activity value : activity) {
//                map = new HashMap<>();
//                map.put("Name", value.name);
//                map.put("Start", value.start);
//                map.put("End", value.end);
//                map.put("Type", value.type);
//                arrayList.add(map);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        for (int count = 0; count < activities.length; count++) {
            if (activities[count] == null) {
                activities[count] = "Пары нет";
            }
        }

        View view = findViewById(activeDayOfWeekId);
        view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.day_of_week_round_corner_active));


        ListView listView = findViewById(R.id.listView);
        SimpleAdapter adapter = new SimpleAdapter(this, arrayList, R.layout.day_item,
                new String[]{"Name", "Start", "End", "Type"},
                new int[]{R.id.activity_name, R.id.activity_start, R.id.activity_end, R.id.activity_type});
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.day_item, R.id.class_name, activities);
        listView.setAdapter(adapter);
    }

    public void changeCurrentWeek(View v) {
        findViewById(activeDayOfWeekId).setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.day_of_week_round_corner));
        System.out.println(v.getResources().getResourceEntryName(v.getId()));
        activeDayOfWeekId = v.getId();
        v.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.day_of_week_round_corner_active));
    }
}