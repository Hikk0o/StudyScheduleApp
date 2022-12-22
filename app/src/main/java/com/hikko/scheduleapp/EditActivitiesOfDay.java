package com.hikko.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.hikko.scheduleapp.adapters.EditActivityAdapter;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;

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

        // Creating layouts from an array
        EditActivityAdapter adapter = new EditActivityAdapter(
                this,
                activitiesOfDayList,
                R.layout.edit_activities_item,
                new String[]{
                        "Name",
                        "Start",
                        "End"},
                new int[]{
                        R.id.ActivityNameAutoCompleteText,
                        R.id.input_time_start_of_activity,
                        R.id.input_time_end_of_activity
                });

        SwipeActionAdapter mAdapter = new SwipeActionAdapter(adapter);
        mAdapter.setListView(activitiesListView);
        // todo Не вписывает в input
        activitiesListView.setAdapter(mAdapter);
        mAdapter.addBackground(SwipeDirection.DIRECTION_NORMAL_LEFT, R.layout.activity_edit_delete_item);
        mAdapter.setNormalSwipeFraction(0.5F);

        Context context = this.getApplicationContext();

        // Listen to swipes
        mAdapter.setSwipeActionListener(new SwipeActionAdapter.SwipeActionListener(){
            @Override
            public boolean hasActions(int position, SwipeDirection direction){
                return direction.isLeft(); // Change this to false to disable left swipes
            }

            @Override
            public boolean shouldDismiss(int position, SwipeDirection direction){
                // Only dismiss an item when swiping normal left
                return direction == SwipeDirection.DIRECTION_NORMAL_LEFT;
            }

            @Override
            public void onSwipe(int[] positionList, SwipeDirection[] directionList){
                for(int i=0;i<positionList.length;i++) {
                    SwipeDirection direction = directionList[i];
                    int position = positionList[i];
                    String dir = "";
//
//                    switch (direction) {
//                        case SwipeDirection.DIRECTION_FAR_LEFT:
//                            dir = "Far left";
//                            break;
//                        case SwipeDirection.DIRECTION_NORMAL_LEFT:
//                            dir = "Left";
//                            break;
//                        case SwipeDirection.DIRECTION_FAR_RIGHT:
//                            dir = "Far right";
//                            break;
//                        case SwipeDirection.DIRECTION_NORMAL_RIGHT:
//                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                            builder.setTitle("Test Dialog").setMessage("You swiped right").create().show();
//                            dir = "Right";
//                            break;
//                    }

                    Toast.makeText(context,
                            dir + " swipe Action triggered on " + mAdapter.getItem(position),
                            Toast.LENGTH_SHORT
                    ).show();

                    mAdapter.notifyDataSetChanged();
                }
            }
        });
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

}