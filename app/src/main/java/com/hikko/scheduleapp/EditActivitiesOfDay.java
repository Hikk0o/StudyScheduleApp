package com.hikko.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hikko.scheduleapp.adapters.EditActivityAdapter;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditActivitiesOfDay extends AppCompatActivity {

    static public ArrayList<HashMap<String, String>> activitiesOfDayList = new ArrayList<>();
    View addActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_activities_of_day);

        ListView activitiesListView = findViewById(R.id.EditActivitiesListView);
        activitiesOfDayList = Utils.getActivitiesDayOfWeek(MainActivity.getActiveDayOfWeek());
        System.out.println("Utils.getActivitiesDayOfWeek");
        updateActivitiesListView();

        // Add button to footer
        addActivityButton = View.inflate(
                this.getLayoutInflater().getContext(),
                R.layout.edit_activities_button,
                null);
        activitiesListView.addFooterView(addActivityButton);

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
        System.out.println(activitiesOfDayList + " updateActivitiesListView");
        List<Activity> activities = new ArrayList<>();
        for (HashMap<String, String> map:
        activitiesOfDayList) {
            Activity activity = Activity.convertToClass(map.get("Name"), map.get("Type"), map.get("Start"), map.get("End"));
            activities.add(activity);
        }

        ArrayAdapter<Activity> adapter = new EditActivityAdapter(
                this, R.layout.edit_activities_item, activities);
        // Creating layouts from an array
//        EditActivityAdapter adapter = new EditActivityAdapter(
//                this,
//                activitiesOfDayList,
//                R.layout.edit_activities_item,
//                new String[]{
//                        "Name",
//                        "Start",
//                        "End"},
//                new int[]{
//                        R.id.ActivityNameAutoCompleteText,
//                        R.id.input_time_start_of_activity,
//                        R.id.input_time_end_of_activity
//                });

        activitiesListView.setAdapter(adapter);
        activitiesListView.setDivider(null);
        activitiesListView.setVerticalScrollBarEnabled(false);

    }

    public void saveActivitiesList(View v) {
        List<List<Activity>> editedActivitiesOfWeek = Utils.getLoadedActivities();
        List<Activity> activities = new ArrayList<>();

        for (HashMap<String, String> activity : activitiesOfDayList) {
            String name = activity.get("Name");
            String start = activity.get("Start");
            String end = activity.get("End");
            String type = activity.get("Type");
            Activity classActivity = Activity.convertToClass(name, type, start, end);
            activities.add(classActivity);
        }
        // todo Вылеты при сохранении для пустых дней
        editedActivitiesOfWeek.set(MainActivity.getActiveDayOfWeek() - 1, activities);
        Utils.setLoadedActivities(editedActivitiesOfWeek);
        Utils.saveWeekToJsonFile(MainActivity.filesDir);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public static void setActivitiesOfDayList(ArrayList<HashMap<String, String>> activities) {
        activitiesOfDayList = activities;
    }

}