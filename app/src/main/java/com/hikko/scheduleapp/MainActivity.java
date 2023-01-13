package com.hikko.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hikko.scheduleapp.adapters.ActivitiesListAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static int activeDayOfWeek = Utils.getLocaleDayOfWeek();
    private static int activeDayOfWeekId = Utils.getIdByDay(activeDayOfWeek);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = findViewById(activeDayOfWeekId);
        changeCurrentWeek(view);
    }

    public void changeCurrentWeek(View v) {
        findViewById(activeDayOfWeekId).setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.day_of_week_round_corner));
        v.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.day_of_week_round_corner_active));

        activeDayOfWeekId = v.getId();
        activeDayOfWeek = Utils.getDayById(activeDayOfWeekId);

        HorizontalScrollView horizontalScrollView = findViewById(R.id.horizontalScrollView);
        horizontalScrollView.post(() -> horizontalScrollView.smoothScrollTo((int) v.getX() - 300, 0));

        if (!Utils.activitiesIsLoaded()) {
            Utils.loadAllActivities(getFilesDir());
        }
        ArrayList<HashMap<String, String>> arrayList = Utils.getActivitiesDayOfWeek(activeDayOfWeek);

        TextView noActivitiesText = findViewById(R.id.no_activities_text);
        if (arrayList != null) {
            if (arrayList.size() == 0) {
                noActivitiesText.setVisibility(View.VISIBLE);
            } else {
                noActivitiesText.setVisibility(View.GONE);
            }
        }

        ListView activitiesListView = findViewById(R.id.ActivitiesListView);
        SimpleAdapter adapter = new ActivitiesListAdapter(this, arrayList, R.layout.day_item,
                new String[]{"Name", "Start", "End", "Type"},
                new int[]{R.id.activity_name, R.id.activity_start, R.id.activity_end, R.id.activity_type},
                this.getResources());

        activitiesListView.setAdapter(adapter);
        activitiesListView.setDivider(null);
        activitiesListView.setVerticalScrollBarEnabled(false);
    }

    public void openEditActivitiesOfDay(View v) {
        Intent intent = new Intent(this, EditActivitiesOfDay.class);
        startActivity(intent);
    }

    public static int getActiveDayOfWeek() {
        return activeDayOfWeek;
    }

}