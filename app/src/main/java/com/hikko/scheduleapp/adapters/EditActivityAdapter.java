package com.hikko.scheduleapp.adapters;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.hikko.scheduleapp.Activity;
import com.hikko.scheduleapp.EditActivitiesOfDay;
import com.hikko.scheduleapp.R;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;
import com.hikko.scheduleapp.Utils;

import java.util.HashMap;
import java.util.List;

public class EditActivityAdapter extends ArraySwipeAdapter<Activity> {

    private final int resourceLayout;
    private final Context mContext;

    public EditActivityAdapter(EditActivitiesOfDay context, int edit_activities_item, List<Activity> activities) {
        super(context, edit_activities_item, activities);
        resourceLayout = edit_activities_item;
        mContext = context;
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            convertView = vi.inflate(resourceLayout, null);
        }

        Activity activity = (Activity) getItem(position);

        View finalConvertView = convertView;

        convertView.findViewById(R.id.SwipeLayout).setOnTouchListener((v, event) -> Utils.clearInputFocus(finalConvertView, mContext));
        convertView.findViewById(R.id.buttonDeleteActivity).setOnTouchListener((v, event) -> {
            deleteActivity(finalConvertView, position, (Activity) getItem(position));
            return false;
        });

        Spinner spinner = convertView.findViewById(R.id.edit_activity_type_spinner);
        if (spinner.getAdapter() == null) {
            ArrayAdapter<CharSequence> s_adapter = ArrayAdapter.createFromResource(convertView.getContext(),
                    R.array.activityType, R.layout.spinner_item);
            s_adapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
            spinner.setAdapter(s_adapter);
        }

        spinner.setSelection(getSpinnerIndex(spinner, activity.type));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position1, long id) {
                HashMap<String, String> temp = EditActivitiesOfDay.activitiesOfDayList.get(position);
                temp.put("Type", (String) spinner.getItemAtPosition(position1));
                EditActivitiesOfDay.activitiesOfDayList.set(position, temp);
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        EditText activityNameText = convertView.findViewById(R.id.ActivityNameAutoCompleteText);
        int maxLength = convertView.getResources().getInteger(R.integer.max_ActivityNameAutoCompleteText);
        InputFilter[] fArray = new InputFilter[2];

        fArray[0] = new InputFilter.LengthFilter(maxLength);
        fArray[1] = (source, start, end, dest, dstart, dend) -> {
            if (source.length() == 0) {
                return null;
            }
            String result = source.toString();

            char c = result.charAt(result.length()-1);

            return c != '\n' ? null : result.replace("\n", "");
        };

        activityNameText.setFilters(fArray);

        activityNameText.setOnFocusChangeListener((v1, hasFocus) -> {
            if (hasFocus) {
                activityNameText.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        HashMap<String, String> temp = EditActivitiesOfDay.activitiesOfDayList.get(position);
                        temp.put("Name", s.toString());
                        EditActivitiesOfDay.activitiesOfDayList.set(position, temp);
                    }
                });

            }
        });

        activityNameText.setText(activity.name);

        EditText input_start_time = convertView.findViewById(R.id.input_time_start_of_activity);
        input_start_time.setText(activity.start);
        EditText input_end_time = convertView.findViewById(R.id.input_time_end_of_activity);
        input_end_time.setText(activity.end);

        InputFilter[] timeFilter = new InputFilter[1];
        timeFilter[0] = (source, start, end, dest, dstart, dend) -> {
            if (source.length() == 0) {
                return null;
            }
            String result = "";
            result = result.concat(dest.toString().substring(0, dstart));
            result = result.concat(source.toString().substring(start, end));
            result = result.concat(dest.toString().substring(dend, dest.length()));

            if (result.length() > 5) {
                return "";
            }
            boolean allowEdit = true;
            char c;
            if (result.length() > 0) {
                c = result.charAt(0);
                allowEdit = (c >= '0' && c <= '2');
            }
            if (result.length() > 1) {
                c = result.charAt(1);
                if (Integer.parseInt(String.valueOf(result.charAt(0))) > 1) {
                    allowEdit &= (c >= '0' && c <= '3');
                } else {
                    allowEdit &= (c >= '0' && c <= '9');
                }
            }
            if (result.length() > 2) {
                c = result.charAt(2);
                allowEdit &= (c == ':');
            }
            if (result.length() > 3) {
                c = result.charAt(3);
                allowEdit &= (c >= '0' && c <= '5');
            }
            if (result.length() > 4) {
                c = result.charAt(4);
                allowEdit &= (c >= '0' && c <= '9');
            }
            return allowEdit ? null : "";
        };

        input_start_time.setFilters(timeFilter);
        input_end_time.setFilters(timeFilter);

        TextWatcher decorationTimeTextWatcher = new TextWatcher() {
            boolean isAddChar = true;

            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    if (isAddChar) {
                        s.append(':');
                    } else {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                if (s.length() == 5) {
                    if (input_start_time.isFocused()) input_end_time.requestFocus();
                }
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isAddChar = before < count;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        };

        input_start_time.addTextChangedListener(decorationTimeTextWatcher);
        input_end_time.addTextChangedListener(decorationTimeTextWatcher);

        input_start_time.setOnFocusChangeListener((v1, hasFocus) -> {
            if (hasFocus) input_start_time.addTextChangedListener(saveInputTimeInList("Start", position));

        });

        input_end_time.setOnFocusChangeListener((v1, hasFocus) -> {
            if (hasFocus) input_end_time.addTextChangedListener(saveInputTimeInList("End", position));
        });
        return convertView;
    }

    private int getSpinnerIndex(Spinner spinner, String myString){
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    private TextWatcher saveInputTimeInList(String key, int position) {
        return new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                HashMap<String, String> temp = EditActivitiesOfDay.activitiesOfDayList.get(position);
                temp.put(key, s.toString());
                EditActivitiesOfDay.activitiesOfDayList.set(position, temp);
            }
        };
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return 0;
    }

    public void deleteActivity(View v, int pos, Activity activity) {
        remove(activity);
        EditActivitiesOfDay.deleteActivity(pos);
        ConstraintLayout finalV = v.findViewById(R.id.rootEditActivity);
        finalV.animate()
                .translationX(v.getWidth() * -1)
                .alpha(0.0f)
                .setDuration(150)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationEnd(@NonNull Animator animation) {
                        finalV.getLayoutTransition().setDuration(30000);
                        finalV.setMaxHeight(0);
                        finalV.setPadding(0,0,0,0);
                    }
                    public void onAnimationStart(@NonNull Animator animation) {}
                    public void onAnimationCancel(@NonNull Animator animation) {}
                    public void onAnimationRepeat(@NonNull Animator animation) {}
                });
    }

}
