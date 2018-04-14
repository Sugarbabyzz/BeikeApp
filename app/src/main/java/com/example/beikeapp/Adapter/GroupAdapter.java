package com.example.beikeapp.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMGroup;
import com.example.beikeapp.R;

import java.util.List;

/**
 * Created by m1821 on 2018/4/12.
 */

public class GroupAdapter extends ArrayAdapter<EMGroup> {

    private LayoutInflater inflater;
    private String newGroup;
    private String addPublicGroup;

    public GroupAdapter(Context context, int res, List<EMGroup> groups) {
        super(context, res, groups);
        this.inflater = LayoutInflater.from(context);
        newGroup = "建群";
        addPublicGroup = "加群";
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if (position == 1) {
            return 1;
        } else if (position == 2) {
            return 2;
        } else {
            return 3;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == 0) {

            if (convertView == null) { //第一栏:建群
                convertView = inflater.inflate(R.layout.em_row_add_group, parent, false);
            }
            ((ImageView) convertView.findViewById(R.id.avatar)).setImageResource(R.drawable.em_create_group);
            ((TextView) convertView.findViewById(R.id.name)).setText(newGroup);
        } else if (getItemViewType(position) == 1) { // 第二栏:加群

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.em_row_add_group, parent, false);
            }
            ((ImageView) convertView.findViewById(R.id.avatar)).setImageResource(R.drawable.em_add_public_group);
            ((TextView) convertView.findViewById(R.id.name)).setText(addPublicGroup);
            ((TextView) convertView.findViewById(R.id.header)).setVisibility(View.VISIBLE);

        } else { //第三栏开始:所在的群列表

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.em_row_group, parent, false);
            }
            ((TextView) convertView.findViewById(R.id.name)).setText(getItem(position - 2).getGroupName());

        }

        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount() + 2;
    }
}
