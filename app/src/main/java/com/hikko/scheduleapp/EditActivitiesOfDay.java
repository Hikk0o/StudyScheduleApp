package com.hikko.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hikko.scheduleapp.adapters.EditActivityAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class EditActivitiesOfDay extends AppCompatActivity {

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_activities_of_day);

        ListView activitiesListView = findViewById(R.id.EditActivitiesListView);

        HashMap<String, String> map = new HashMap<>();

        map.put("123", "123");
        arrayList.add(map);

        updateActivitiesListView();

        // Add button to footer
        View addActivityButton = View.inflate(this.getLayoutInflater().getContext(), R.layout.edit_activities_button, null);
        activitiesListView.addFooterView(addActivityButton);


    }

    @Override
    protected void onStart() {
        super.onStart();
//        DialogFragment newFragment = new TimePickerFragment();
//        newFragment.show(getSupportFragmentManager(), "timePicker");

    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    public void goBackActivities(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void createNewActivity(View v) {
        HashMap<String, String> map = new HashMap<>();

        map.put("123", "123");
        arrayList.add(map);
        updateActivitiesListView();
    }

    private void updateActivitiesListView() {
        ListView activitiesListView = findViewById(R.id.EditActivitiesListView);
        // Creating layouts from an array
        SimpleAdapter adapter = new EditActivityAdapter(this, arrayList, R.layout.edit_activities_item,
                new String[]{},
                new int[]{});
        activitiesListView.setAdapter(adapter);
    }
}