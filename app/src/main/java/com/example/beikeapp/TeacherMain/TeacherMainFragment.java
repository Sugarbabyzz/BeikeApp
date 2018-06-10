package com.example.beikeapp.TeacherMain;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.beikeapp.R;
import com.example.beikeapp.TeacherMain.Homework.AssignHomeworkWholesale;
import com.example.beikeapp.TeacherMain.Notify.TeacherMainNotify;


/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class TeacherMainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView lvFunctionList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_teacher_main, null);
        initView(view);

        return view;
    }

    private void initView(View view) {
        lvFunctionList = view.findViewById(R.id.lv_shiShengFunctionList);
        String[] functionList = new String[]{"发布通知", "发布作业", "发布评教", "作业完成情况", "师生群聊"};
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, functionList);
        lvFunctionList.setAdapter(mAdapter);

        lvFunctionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        startActivity(new Intent(getActivity(), TeacherMainNotify.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), AssignHomeworkWholesale.class));
                        break;
                }
            }
        });
    }


}
