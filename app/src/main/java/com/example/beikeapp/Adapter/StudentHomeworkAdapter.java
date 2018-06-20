package com.example.beikeapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.beikeapp.R;
import com.example.beikeapp.StudentMain.Homework.StudentHomework;

import java.util.List;

public class StudentHomeworkAdapter extends ArrayAdapter<StudentHomework> {

    private static final String TAG = "StudentHomeworkAdapter";

    private LayoutInflater inflater;

    private Context context;

    public StudentHomeworkAdapter(@NonNull Context context, int resource, @NonNull List<StudentHomework> objects) {
        super(context, resource, objects);

        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_student_homework, parent, false);
        }
        // title

        ((TextView) convertView.findViewById(R.id.student_homework_item)).setText(getItem(position).getTitle());


        return convertView;
    }

}
