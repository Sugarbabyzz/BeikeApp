package com.example.beikeapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.beikeapp.R;
import com.hyphenate.chat.EMGroup;

import java.util.List;

public class StudentGroupAdapter extends ArrayAdapter<EMGroup> {

    private LayoutInflater inflater;

    public StudentGroupAdapter(Context context, int res, List<EMGroup> groups) {
        super(context, res, groups);
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.em_row_group, parent, false);
            }
            ((TextView) convertView.findViewById(R.id.name)).setText(getItem( position ).getGroupName());


        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}


