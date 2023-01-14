package com.hikko.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.hikko.scheduleapp.adapters.EditActivityAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EditActivitiesOfDay extends AppCompatActivity {

    static public List<Activity> activitiesOfDayList = new ArrayList<>();
    View addActivityButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_activities_of_day);

        ListView activitiesListView = findViewById(R.id.EditActivitiesListView);

        ArrayList<Activity> tempList = ActivityUtils.getActivitiesDayOfWeek(MainActivity.getActiveDayOfWeek());
        activitiesOfDayList.clear();
        if (tempList != null) {
            for (Activity activity : tempList) {
                activitiesOfDayList.add(activity.clone());
            }
        }
        updateActivitiesListView();

        // Add button to footer
        addActivityButton = View.inflate(
                this.getLayoutInflater().getContext(),
                R.layout.edit_activities_button,
                null);
        activitiesListView.addFooterView(addActivityButton);

        findViewById(R.id.backround).setOnTouchListener((v, event) -> ActivityUtils.clearInputFocus(activitiesListView, activitiesListView.getContext()));


    }

    public void goBackActivities(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        goBackActivities(new View(getApplicationContext()));
    }

    public void createNewActivity(View view) {

        activitiesOfDayList.add(new Activity());
        updateActivitiesListView();

        int maxActivities = view.getResources().getInteger(R.integer.max_EditActivities);
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
        ArrayList<Activity> activities = new ArrayList<>();
        int count = 0;
        for (Activity activity:
            activitiesOfDayList) {
                activity.setId(count++);
                activities.add(activity);
        }

        EditActivityAdapter adapter = new EditActivityAdapter(
                this, R.layout.edit_activities_item, activities);

        activitiesListView.setAdapter(adapter);
        activitiesListView.setDivider(null);
        activitiesListView.setVerticalScrollBarEnabled(false);

    }

    public void saveActivitiesList(View view) {
        ArrayList<ArrayList<Activity>> editedActivitiesOfWeek = ActivityUtils.getLoadedActivities();
        ArrayList<Activity> activities = new ArrayList<>();

        for (Activity activity : activitiesOfDayList) {
            System.out.println(activity);
            String start = activity.getStartTime();
            String end = activity.getEndTime();
            if (start == null || end == null || start.length() < 4 || end.length() < 4) {
                Toast.makeText(
                        this,
                        "Сначала заполните время для пар",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }
            activities.add(activity);
        }
        activities.sort(Comparator.comparing(Activity::getStartTime));

        editedActivitiesOfWeek.set(MainActivity.getActiveDayOfWeek(), activities);
        ActivityUtils.setLoadedActivities(editedActivitiesOfWeek);
        ActivityUtils.saveWeekToJsonFile(view.getContext().getFilesDir());

        ActivitiesDayWidget.updateWidget(getApplicationContext());

        goBackActivities(view);
    }

    public static void deleteActivity(int pos) {
        activitiesOfDayList.remove(pos);
    }
}