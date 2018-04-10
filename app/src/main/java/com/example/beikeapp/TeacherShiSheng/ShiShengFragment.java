package com.example.beikeapp.TeacherShiSheng;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.beikeapp.R;

import java.util.zip.Inflater;

import cn.smssdk.gui.layout.ListviewTitleLayout;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShiShengFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShiShengFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView lvFunctionList;

    public ShiShengFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShiShengFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShiShengFragment newInstance(String param1, String param2) {
        ShiShengFragment fragment = new ShiShengFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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

        View view = inflater.inflate(R.layout.fragment_shisheng,null);
        initView(view);

        return view;
    }

    private void initView(View view) {
        lvFunctionList = view.findViewById(R.id.lv_shiShengFunctionList);
        String[] functionList = new String[]{"发布通知","发布作业","发布评教","作业完成情况","师生群聊"};
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1,functionList);
        lvFunctionList.setAdapter(mAdapter);
    }


}
