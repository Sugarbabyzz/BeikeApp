package com.example.beikeapp.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.beikeapp.R;
import com.example.beikeapp.TeacherMain.Homework.Homework;

import java.util.List;

/**
 * Created by m1821 on 2018/6/5.
 */

public class HomeworkAdapter extends ArrayAdapter<Homework> {

    private LayoutInflater inflater;

    /**
     * Edit button
     */
    private Button btnEdit;

    /**
     * Delete button
     */
    private Button btnDelete;

    public HomeworkAdapter(@NonNull Context context, int resource, @NonNull List<Homework> homeworkList) {
        super(context, resource, homeworkList);
        this.inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_homework, parent, false);
        }
        // subject
        Spannable strSubject = new SpannableString((position + 1) + "." + getItem(position).getSubject());
        strSubject.setSpan(new StyleSpan(Typeface.BOLD),0,2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        strSubject.setSpan(new RelativeSizeSpan(1.3f),0,2,Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ((TextView) convertView.findViewById(R.id.subject)).setText(strSubject);

        // option a-d
        ((TextView) convertView.findViewById(R.id.option_a)).setText(getItem(position).getOptionA());
        ((TextView) convertView.findViewById(R.id.option_b)).setText(getItem(position).getOptionB());
        ((TextView) convertView.findViewById(R.id.option_c)).setText(getItem(position).getOptionC());
        ((TextView) convertView.findViewById(R.id.option_d)).setText(getItem(position).getOptionD());

        //key
        ((TextView) convertView.findViewById(R.id.key)).setText(getItem(position).getKey() + "");



        return convertView;
    }
}
