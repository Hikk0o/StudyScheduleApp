package com.hikko.scheduleapp.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.hikko.scheduleapp.R;

import java.util.List;
import java.util.Map;

public class EditActivityAdapter extends SimpleAdapter {

    private final int resourceLayout;
    private final Context mContext;

    public EditActivityAdapter(Context context, List<? extends Map<String, ?>> data,
                               @LayoutRes int resource, String[] from, @IdRes int[] to) {
        super(context, data, resource, from, to);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Spinner spinner = v.findViewById(R.id.activity_type_spinner);
        ArrayAdapter<CharSequence> s_adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.activityType, R.layout.spinner_item);
        s_adapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        spinner.setAdapter(s_adapter);

        TextView activityNameAutoCompleteText = v.findViewById(R.id.ActivityNameAutoCompleteText);
        int maxLength = v.getResources().getInteger(R.integer.max_ActivityNameAutoCompleteText);
        InputFilter[] fArray = new InputFilter[2];

        fArray[0] = new InputFilter.LengthFilter(maxLength);

        fArray[1] = (source, start, end, dest, dstart, dend) -> {
            if (source.length() == 0) {
                return null;
            }
            String result = source.toString();
//            result = result.concat(dest.toString().substring(0, dstart));
//            result = result.concat(source.toString().substring(start, end));
//            result = result.concat(dest.toString().substring(dend, dest.length()));

            boolean allowEdit;
            char c;

            c = result.charAt(result.length()-1);
            allowEdit = c != '\n';

            return allowEdit ? null : result.replace("\n", "");
        };

        activityNameAutoCompleteText.setFilters(fArray);

        EditText input_start_time = v.findViewById(R.id.input_time_start_of_activity);
        EditText input_end_time = v.findViewById(R.id.input_time_end_of_activity);

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
                allowEdit &= (c >= '0' && c <= '9');
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

        return v;
    }

}
