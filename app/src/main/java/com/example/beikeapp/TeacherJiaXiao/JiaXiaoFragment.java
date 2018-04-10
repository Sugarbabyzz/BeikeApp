package com.example.beikeapp.TeacherJiaXiao;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.Constant.TeacherConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.MyAsyncTask;
import com.hyphenate.chat.EMClient;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JiaXiaoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JiaXiaoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView lvClassList;

    public JiaXiaoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JiaXiaoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JiaXiaoFragment newInstance(String param1, String param2) {
        JiaXiaoFragment fragment = new JiaXiaoFragment();
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

        View view = inflater.inflate(R.layout.fragment_jiaxiao, null);

        lvClassList = view.findViewById(R.id.lv_jiaXiaoClassList);

        getClassList(EMClient.getInstance().getCurrentUser());

        return view;
    }


    private void getClassList(String currentUser) {
        String urlString = TeacherConstant.URL_BASIC + TeacherConstant.URL_GET_CLASS_LIST
                + "?account=" + currentUser;


        MyAsyncTask a = new MyAsyncTask(getActivity());
        a.execute(urlString);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {

                //listData.get(0)的数据格式: SUCCESS/六年级(2)班,四年级(3)班,五年级(7)班,...
                //                           FAIL/null
                String[] responseArray = listData.get(0).split("/");

                //获取成功,做适配
                if (responseArray[0].equals(GlobalConstant.FLAG_SUCCESS)) {
                    //获得班级列表数组并适配至ListView中
                    String[] classArray = responseArray[1].split(",");
                    ArrayAdapter<String> mAdapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_list_item_1,classArray);
                    lvClassList.setAdapter(mAdapter);
                }
                //获取失败，提示
                else if (responseArray[0].equals(GlobalConstant.FLAG_FAILURE)) {
                    Toast.makeText(getContext(),"获取班级列表失败!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDataReceivedFailed() {
            }
        });
    }

}
