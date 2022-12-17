package com.hikko.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private int dayOfWeek = getDayOfWeek();
    private int activeDayOfWeekId = Utils.getIdByDay(dayOfWeek);

    private int getDayOfWeek() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        return dayOfWeek.getValue();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.loadAllActivities(getFilesDir());
        Utils.saveWeekToJsonFile(getFilesDir());

        View view = findViewById(activeDayOfWeekId);

        changeCurrentWeek(view);
    }

    public void changeCurrentWeek(View v) {
        findViewById(activeDayOfWeekId).setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.day_of_week_round_corner));
        v.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.day_of_week_round_corner_active));

        activeDayOfWeekId = v.getId();
        dayOfWeek = Utils.getDayById(activeDayOfWeekId);


        ArrayList<HashMap<String, String>> arrayList = Utils.getActivitiesDayOfWeek(dayOfWeek);
        TextView noActivitiesView = findViewById(R.id.no_activities_text);
        if (arrayList.size() == 0) {
            noActivitiesView.setVisibility(View.VISIBLE);
        } else {
            noActivitiesView.setVisibility(View.GONE);
        }
        ListView listView = findViewById(R.id.listView);
        SimpleAdapter adapter = new SimpleAdapter(this, arrayList, R.layout.day_item,
                new String[]{"Name", "Start", "End", "Type"},
                new int[]{R.id.activity_name, R.id.activity_start, R.id.activity_end, R.id.activity_type});

        listView.setAdapter(adapter);
    }
}