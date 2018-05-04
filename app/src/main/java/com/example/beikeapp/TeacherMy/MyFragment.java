package com.example.beikeapp.TeacherMy;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.Constant.ParentConstant;
import com.example.beikeapp.Constant.StudentConstant;
import com.example.beikeapp.Constant.TeacherConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.MyAsyncTask;
import com.example.beikeapp.Util.ProfileUtil.ProfileActivity;
import com.hyphenate.chat.EMClient;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String id;
    String name,gender,school,classes;
    RelativeLayout rlProfile;
    RelativeLayout rlSetting;
    TextView tvName;
    ImageView ivPhoto;

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
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
        View view = inflater.inflate(R.layout.my_main,null);
        initView(view);

        id = "Teacher";

        String url = assembleUrl(id);
        setupValue(url);

        return view;
    }

    private void setupValue(String url) {
        MyAsyncTask a = new MyAsyncTask(getActivity());
        a.execute(url);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {
                //listData.get(0)的数据格式：SUCCESS/泰勒/头像图片URL
                //                 FAIL/null
                String[] resultArray = listData.get(0).split("/");
                if (resultArray[0].equals(GlobalConstant.FLAG_SUCCESS)){
                    name = resultArray[1];
                    gender = resultArray[3];
                    school = resultArray[4];
                    classes = resultArray[5];
                    tvName.setText(name);
                    //later for profile photo url.
                }
                else if (listData.get(0).equals(GlobalConstant.FLAG_FAILURE)){
                    Toast.makeText(getActivity(),"信息获取失败", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(),"信息获取错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDataReceivedFailed() {

            }
        });
    }

    private void initView(View view) {
        rlProfile = view.findViewById(R.id.rl_profile);
        rlSetting = view.findViewById(R.id.rl_setting);
        tvName = view.findViewById(R.id.tv_profile_name);
        ivPhoto = view.findViewById(R.id.img_profile_photo);
        rlProfile.setOnClickListener(this);
        rlSetting.setOnClickListener(this);
    }

    /**
     * 组装URL，三种身份
     * @param id 身份
     * @return 返回值URL
     */
    private String assembleUrl(String id) {
        String url = null;
        switch (id){
            case "Teacher":
                url = TeacherConstant.URL_BASIC + TeacherConstant.URL_GET_GENERAL_INFO
                        +"?id=" + id
                        +"&account=" + EMClient.getInstance().getCurrentUser();
                break;
            case "Student":
                url = StudentConstant.URL_GetGeneralInfo
                        +"?id=" + id
                        +"&account=" + EMClient.getInstance().getCurrentUser();
                break;
            case "Parent":
                url = ParentConstant.URL_Get_General_Info
                        +"?id=" + id
                        +"&account=" + EMClient.getInstance().getCurrentUser();
                break;
        }

        return url;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_profile:
                startActivity(new Intent(getActivity(), ProfileActivity.class)
                        .putExtra("id",id)
                        .putExtra("name",name)
                        .putExtra("gender",gender)
                        .putExtra("school",school)
                        .putExtra("classes",classes));
                break;
            case R.id.rl_setting:
                break;
        }
    }
}
