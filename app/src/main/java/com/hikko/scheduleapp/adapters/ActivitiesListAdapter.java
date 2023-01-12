package com.hikko.scheduleapp.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.widget.SimpleAdapter;

import com.hikko.scheduleapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivitiesListAdapter extends SimpleAdapter {

    public ActivitiesListAdapter(Context context, List<HashMap<String, String>> data, int resource, String[] from, int[] to, Resources res) {
        super(context, data, resource, from, to);
        for (Map<String, String> map : data) {
            String name = map.get("Name");
            if (name != null) {
                if (name.length() == 0) {
                    map.put("Name", res.getString(R.string.empty_name_activity));
                    map.put("Type", null);
                }
            } else {
                map.put("Name", res.getString(R.string.empty_name_activity));
                map.put("Type", null);
            }
        }
    }
}
