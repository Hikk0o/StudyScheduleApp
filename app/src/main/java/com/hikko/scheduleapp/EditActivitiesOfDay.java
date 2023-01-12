package com.hikko.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.hikko.scheduleapp.adapters.EditActivityAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class EditActivitiesOfDay extends AppCompatActivity {

    static public ArrayList<HashMap<String, String>> activitiesOfDayList = new ArrayList<>();
    View addActivityButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_activities_of_day);

        ListView activitiesListView = findViewById(R.id.EditActivitiesListView);
        activitiesOfDayList = Utils.getActivitiesDayOfWeek(MainActivity.getActiveDayOfWeek());
        updateActivitiesListView();

        // Add button to footer
        addActivityButton = View.inflate(
                this.getLayoutInflater().getContext(),
                R.layout.edit_activities_button,
                null);
        activitiesListView.addFooterView(addActivityButton);

        findViewById(R.id.backround).setOnTouchListener((v, event) -> Utils.clearInputFocus(activitiesListView, activitiesListView.getContext()));


    }

    public void goBackActivities(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void createNewActivity(View v) {
        HashMap<String, String> map = new HashMap<>();

        activitiesOfDayList.add(map);
        updateActivitiesListView();

        int maxActivities = v.getResources().getInteger(R.integer.max_EditActivities);
        if (activitiesOfDayList.size() >= maxActivities) {
            ListView activitiesListView = findViewById(R.id.EditActivitiesListView);
            activitiesListView.removeFooterView(addActivityButton);
        }

        // Scrolling to new item
        ListView activitiesListView = findViewById(R.id.EditActivitiesListView);
        activitiesListView.setSelection(activitiesOfDayList.size() - 1);
    }

    private void updateActivitiesListView() {
        ListView activitiesListView = findViewById(R.id.EditActivitiesListView);
        List<Activity> activities = new ArrayList<>();
        int count = 0;
        for (HashMap<String, String> map:
            activitiesOfDayList) {
                Log.i("updateActivitiesListView", String.valueOf(map));
                Activity activity = new Activity(map.get("Name"), map.get("Type"), map.get("Start"), map.get("End"), count++);
                activities.add(activity);
            }

        EditActivityAdapter adapter = new EditActivityAdapter(
                this, R.layout.edit_activities_item, activities);



        activitiesListView.setAdapter(adapter);
        activitiesListView.setDivider(null);
        activitiesListView.setVerticalScrollBarEnabled(false);

    }

    public void saveActivitiesList(View v) {
        List<List<Activity>> editedActivitiesOfWeek = Utils.getLoadedActivities();
        List<Activity> activities = new ArrayList<>();

        for (HashMap<String, String> listActivity : activitiesOfDayList) {
            String name = listActivity.get("Name");
            String start = listActivity.get("Start");
            String end = listActivity.get("End");
            if (start == null || end == null || start.length() < 4 || end.length() < 4) {
                Toast.makeText(
                        this,
                        "Сначала заполните время для пар",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }
            String type = listActivity.get("Type");
            Activity activity = new Activity(name, type, start, end);
            activities.add(activity);
        }
        activities.sort(Comparator.comparing(Activity::getStartTime));

        editedActivitiesOfWeek.set(MainActivity.getActiveDayOfWeek() - 1, activities);
        Utils.setLoadedActivities(editedActivitiesOfWeek);
        Utils.saveWeekToJsonFile(MainActivity.filesDir);


        ActivitiesDayWidget.updateWidget(getApplicationContext());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public static void deleteActivity(int pos) {
        activitiesOfDayList.remove(pos);
    }
}