package com.example.beikeapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.beikeapp.R;
import com.example.beikeapp.StudentNotify.Notify.Notify;

import java.util.List;

public class NotifyAdapter extends ArrayAdapter<Notify> {

    private static final String TAG = "NotifyAdapter";

    private LayoutInflater inflater;

    private Context context;

    public NotifyAdapter(@NonNull Context context, int resource, @NonNull List<Notify> notifyList) {
        super(context, resource, notifyList);
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_notify, parent, false);
        }
        // title

        ((TextView) convertView.findViewById(R.id.notify_item)).setText(getItem(position).getTitle());


        return convertView;
    }
}
