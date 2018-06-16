package com.example.beikeapp.StudentSetting;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.StudentMain.StudentMain;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.MyAsyncTask;
import com.example.beikeapp.Util.ProfileUtil.ProfileActivity;
import com.example.beikeapp.Util.ProfileUtil.ProfileInfo;
import com.example.beikeapp.Util.ProfileUtil.SettingActivity;
import com.hyphenate.chat.EMClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment implements View.OnClickListener{
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
    private SwipeRefreshLayout swipeRefreshLayout;
    //student only
    private RelativeLayout rlCorrectionBook;


    /**
     * sdk api >= 23, 执行onAttach(context)，不执行onAttach(Activity activity)
     * [注]onAttach先于onCreate方法调用
     * 故id字段一开始就获取到了
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 获取到身份
        id = ((StudentMain)context).getBaseId();

    }

    /**
     * sdk api < 23, 执行onAttach(activity)，不执行onAttach(Context context)
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //获取到身份
        id = ((StudentMain)activity).getBaseId();
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
        View view = inflater.inflate(R.layout.my_main, null);

        initView(view);

        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("努力获取中");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        //get image
        OneThread tt = new OneThread();
        tt.start();
        try {
            tt.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // get personal info
        getInfoFromServer();

        return view;
    }

    public class OneThread extends Thread{

        @Override
        public void run() {
            getImageFromServer();
        }
    }


    /**
     * 获取个人信息(除图片外)
     */
    private void getInfoFromServer() {

        String url = GlobalConstant.URL_GET_INFO
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
                    // save all the info into a class for further use
                    new ProfileInfo().setInfoAsNonParent(name,gender,school,classes,bitmap);

                    setUpView();
                    progressDialog.dismiss();

                } else if (resultArray[0].equals(GlobalConstant.FLAG_FAILURE)) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "信息获取失败", Toast.LENGTH_SHORT).show();

                } else {
                    progressDialog.dismiss();
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
     * 并获取至全局的bitmap
     */
    private void getImageFromServer() {

        InputStream inputStream = null;
        try {
            //得到io流
            inputStream = getInputStreamFromURL(GlobalConstant.URL_GET_PHOTO + "?id=" + id
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
     * setup ui
     */
    private void setUpView() {
        //name
        tvName.setText(ProfileInfo.name);
        //photo
        if (ProfileInfo.isSet){
            ivPhoto.setImageBitmap(ProfileInfo.bitmap);
        } else {
            ivPhoto.setBackgroundResource(R.drawable.bg_border);
        }
    }

    /**
     * 从服务器获取图片输入流
     * @param urlStr 服务器url
     *
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
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout_my_main);
        rlProfile = view.findViewById(R.id.rl_profile);
        rlSetting = view.findViewById(R.id.rl_setting);
        rlCorrectionBook = view.findViewById(R.id.rl_correction_book);
        rlCorrectionBook.setVisibility(View.VISIBLE);
        tvName = view.findViewById(R.id.tv_profile_name);
        ivPhoto = view.findViewById(R.id.img_profile_photo);
        rlProfile.setOnClickListener(this);
        rlSetting.setOnClickListener(this);
        rlCorrectionBook.setOnClickListener(this);
        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OneThread tt = new OneThread();
                tt.start();
                try {
                    tt.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                getInfoFromServer();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_profile:
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                break;
            case R.id.rl_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.rl_correction_book:
                Toast.makeText(getActivity(),"NOT DONE YET!",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
