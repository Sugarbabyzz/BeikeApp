package com.example.beikeapp.TeacherMy;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.TeacherMain.TeacherMainActivity;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.MyAsyncTask;
import com.example.beikeapp.Util.ProfileUtil.BitmapStore;
import com.example.beikeapp.Util.ProfileUtil.ProfileActivity;
import com.hyphenate.chat.EMClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    private String id; // 用户身份
    private String name, gender, school, classes;
    private RelativeLayout rlProfile;
    private RelativeLayout rlSetting;
    private TextView tvName;
    private ImageView ivPhoto;
    private Bitmap bitmap = null;
    private ProgressDialog progressDialog;

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

    /**
     * sdk api >= 23, 执行onAttach(context)，不执行onAttach(Activity activity)
     * [注]onAttach先于onCreate方法调用
     * 故id字段一开始就获取到了
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 获取到身份
        id = ((TeacherMainActivity) context).getBaseId();

    }

    /**
     * sdk api < 23, 执行onAttach(activity)，不执行onAttach(Context context)
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //get id from BaseActivity
        id = ((TeacherMainActivity) activity).getBaseId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_main, null);



        initView(view);
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("努力获取中");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
        // get information from our server
        // takes time.
        new Thread(new Runnable() {
            @Override
            public void run() {
                // get personal info
                getInfoFromServer();
                // get profile photo
                getImageFromServer();
                // the way to use runOnUiThread in Fragment
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //name
                        tvName.setText(name);
                        //photo
                        if (bitmap != null){
                            ivPhoto.setImageBitmap(bitmap);
                            // save bitmap for further use
                            new BitmapStore().setBitmap(bitmap);
                        } else {
                            ivPhoto.setBackgroundResource(R.drawable.bg_border);
                        }
                        //dismiss dialog
                        progressDialog.dismiss();
                    }
                });
            }
        }).start();

        return view;
    }

    /**
     * 获取个人信息(除图片外)
     */
    private void getInfoFromServer() {

        String url = GlobalConstant.URL_GET_GENERAL_INFO
                + "?id=" + id
                + "&account=" + EMClient.getInstance().getCurrentUser();

        MyAsyncTask a = new MyAsyncTask(getActivity());
        a.execute(url);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {
                //listData.get(0)的数据格式：SUCCESS/头像图片URL/泰勒/男/苏州立达中学/初三(5)班,初二(7)班
                //                 FAIL/null
                String[] resultArray = listData.get(0).split("/");
                if (resultArray[0].equals(GlobalConstant.FLAG_SUCCESS)) {
                    name = resultArray[2];
                    gender = resultArray[3];
                    school = resultArray[4];
                    classes = resultArray[5];

                } else if (resultArray[0].equals(GlobalConstant.FLAG_FAILURE)) {
                    Toast.makeText(getActivity(), "信息获取失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "信息获取错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDataReceivedFailed() {

            }
        });
    }

    /**
     * 获取服务器图片输入流
     */
    private void getImageFromServer() {
        InputStream inputStream = null;
        try {
            //得到io流
            inputStream = getInputStreamFromURL(GlobalConstant.URL_GET_PROFILE_PHOTO + "?id=" + id
                    + "&account=" + EMClient.getInstance().getCurrentUser());
            bitmap = BitmapFactory.decodeStream(inputStream);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从服务器获取图片输入流并解析成bitmap
     * 并set好bitmap
     *
     * @param urlStr
     * @return
     */
    public InputStream getInputStreamFromURL(String urlStr) {

        HttpURLConnection urlConn;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            urlConn = (HttpURLConnection) url.openConnection();
            inputStream = urlConn.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return inputStream;
    }

    private void initView(View view) {
        rlProfile = view.findViewById(R.id.rl_profile);
        rlSetting = view.findViewById(R.id.rl_setting);
        tvName = view.findViewById(R.id.tv_profile_name);
        ivPhoto = view.findViewById(R.id.img_profile_photo);
        rlProfile.setOnClickListener(this);
        rlSetting.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_profile:
                startActivity(new Intent(getActivity(), ProfileActivity.class)
                        .putExtra("name", name)
                        .putExtra("gender", gender)
                        .putExtra("school", school)
                        .putExtra("classes", classes));
                break;
            case R.id.rl_setting:
                break;
        }
    }
}
