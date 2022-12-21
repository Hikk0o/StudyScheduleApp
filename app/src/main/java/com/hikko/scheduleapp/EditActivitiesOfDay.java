package com.hikko.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hikko.scheduleapp.adapters.ListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class EditActivitiesOfDay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_activities_of_day);

        ListView activitiesListView = findViewById(R.id.EditActivitiesListView);

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();

        map.put("123", "123");
        arrayList.add(map);
        arrayList.add(map);

        SimpleAdapter adapter = new ListAdapter(this, arrayList, R.layout.edit_activities_item,
                new String[]{},
                new int[]{});
        activitiesListView.setAdapter(adapter);


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
}