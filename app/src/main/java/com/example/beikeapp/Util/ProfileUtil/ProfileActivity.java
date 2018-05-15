package com.example.beikeapp.Util.ProfileUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.Constant.ParentConstant;
import com.example.beikeapp.Constant.StudentConstant;
import com.example.beikeapp.Constant.TeacherConstant;
import com.example.beikeapp.LoginPage.ForgetPswActivity;
import com.example.beikeapp.LoginPage.LoginActivity;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.BaseActivity;
import com.example.beikeapp.Util.ChatUtil.GroupDetailsActivity;
import com.example.beikeapp.Util.MyAsyncTask;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    RelativeLayout rlProfilePhoto;
    RelativeLayout rlProfileName;
    RelativeLayout rlProfileGender;
    RelativeLayout rlProfileSchool;
    RelativeLayout rlProfileClass;
    TextView tvProfileName;
    TextView tvProfileGender;
    TextView tvProfileSchool;
    TextView tvProfileClass;

    String id;
    String pfName;
    String pfGender;
    String pfSchool;
    String pfClasses;

    private static final String TAG = "ProfileActivity";

    private ProgressDialog progressDialog;


    private static final int REQUEST_CODE_EDIT_PROFILE_NAME = 1;
    private static final int REQUEST_CODE_EDIT_PROFILE_PHOTO = 2;
    private static final int REQUEST_CODE_EDIT_PROFILE_GENDER = 3;
    private static final int REQUEST_CODE_EDIT_PROFILE_CLASS = 4;
    private static final int REQUEST_CODE_EDIT_PROFILE_SCHOOL = 5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
        setContentView(R.layout.my_profile_main);

        initView();

        id = BaseId;
        pfName = getIntent().getStringExtra("name");
        pfGender = getIntent().getStringExtra("gender");

        tvProfileName.setText(pfName);
        tvProfileGender.setText(pfGender);
        //学生和老师的身份，还需要获取school,classes字段
        if (id.equals(GlobalConstant.ID_TEACHER) || id.equals(GlobalConstant.ID_STUDENT)){
            pfSchool = getIntent().getStringExtra("school");
            pfClasses = getIntent().getStringExtra("classes");
            tvProfileSchool.setText(pfSchool);
            tvProfileClass.setText(pfClasses);
        }



    }

    private void initView() {
        rlProfilePhoto = findViewById(R.id.rl_profile_photo);
        rlProfileName = findViewById(R.id.rl_profile_name);
        rlProfileGender = findViewById(R.id.rl_profile_gender);
        rlProfileSchool = findViewById(R.id.rl_profile_school);
        rlProfileClass = findViewById(R.id.rl_profile_class);

        tvProfileName = findViewById(R.id.tv_profile_name);
        tvProfileGender = findViewById(R.id.tv_profile_gender);
        tvProfileSchool = findViewById(R.id.tv_profile_school);
        tvProfileClass = findViewById(R.id.tv_profile_class);

        rlProfileClass.setOnClickListener(this);
        rlProfileSchool.setOnClickListener(this);
        rlProfileGender.setOnClickListener(this);
        rlProfileName.setOnClickListener(this);
        rlProfilePhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.rl_profile_photo:
                break;
            case R.id.rl_profile_name: // 更改姓名
                startActivityForResult(new Intent(ProfileActivity.this,ProfileDetailsEditActivity.class)
                                .putExtra("title","编辑姓名")
                                .putExtra("data",pfName),
                        REQUEST_CODE_EDIT_PROFILE_NAME);
                break;
            case R.id.rl_profile_school: // 更改学校名
                startActivityForResult(new Intent(ProfileActivity.this,ProfileDetailsEditActivity.class)
                                .putExtra("title","编辑学校名")
                                .putExtra("data",pfSchool),
                        REQUEST_CODE_EDIT_PROFILE_SCHOOL);
                break;
            case R.id.rl_profile_gender: //更改性别
                startActivityForResult(new Intent(ProfileActivity.this,ProfileDetailsEditGenderActivity.class)
                                .putExtra("data",pfGender),
                        REQUEST_CODE_EDIT_PROFILE_GENDER);
                break;
            case R.id.rl_profile_class: //更改班级
                startActivityForResult(new Intent(ProfileActivity.this,ProfileDetailsEditClassActivity.class)
                                .putExtra("data",pfClasses),
                        REQUEST_CODE_EDIT_PROFILE_CLASS);
                break;
        }
    }

    /**
     * 点击编辑页面的保存按钮，调用该方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(ProfileActivity.this);
                progressDialog.setMessage("don't know what this string is for..");
                progressDialog.setCanceledOnTouchOutside(false);
            }
            switch (requestCode) {
                case REQUEST_CODE_EDIT_PROFILE_NAME: //修改姓名
                    final String name = data.getStringExtra("data");
                    if (!TextUtils.isEmpty(name)){
                        progressDialog.setMessage("正在修改姓名...");
                        progressDialog.show();
                    }
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                ChangeInfoTask(ProfileActivity.this,BaseId,"Name",name);

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "fail to change name!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                    break;
                case REQUEST_CODE_EDIT_PROFILE_SCHOOL: //修改学校名
                    final String schoolName = data.getStringExtra("data");
                    if (!TextUtils.isEmpty(schoolName)) {
                        progressDialog.setMessage("正在修改学校名...");
                        progressDialog.show();
                    }

                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                ChangeInfoTask(ProfileActivity.this,BaseId,"School",schoolName);

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "fail to change School!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                    break;

                case REQUEST_CODE_EDIT_PROFILE_GENDER: //修改性别
                    final String gender = data.getStringExtra("data");
                    if (!TextUtils.isEmpty(gender)) {
                        progressDialog.setMessage("正在修改性别...");
                        progressDialog.show();
                    }

                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                ChangeInfoTask(ProfileActivity.this,BaseId,"Sex",gender);

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "fail to change Gender!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                    break;
                case REQUEST_CODE_EDIT_PROFILE_CLASS: //修改班级
                    final ArrayList classesList = data.getStringArrayListExtra("data");
                    if (classesList.isEmpty()) {
                        progressDialog.setMessage("正在修改班级...");
                        progressDialog.show();
                    }

                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                String classes = StringUtils.join(classesList, ",");
                                ChangeInfoTask(ProfileActivity.this,BaseId,"Class",classes);

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "fail to change Class!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                    break;

            }
        }

    }

    /**
     * 更新数据库信息
     * @param context
     */
    private void ChangeInfoTask(Context context, String ida, final String columnName, final String data) {

        String url = GlobalConstant.URL_CHANGE_PROFILE_INFO  + "?id=" + id
                + "&account=" + EMClient.getInstance().getCurrentUser()
                + "&columnName=" + columnName
                + "&data=" + data;
        MyAsyncTask a = new MyAsyncTask(context);
        a.execute(url);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {

                //修改成功
                if (listData.get(0).equals(GlobalConstant.FLAG_SUCCESS)) {
                    switch (columnName){
                        case "Name":
                            tvProfileName.setText(data);
                            break;
                        case "School":
                            tvProfileSchool.setText(data);
                            break;
                        case "Sex":
                            tvProfileGender.setText(data);
                            break;
                        case "Class":
                            tvProfileClass.setText(data);
                            break;
                        default:
                            break;
                    }

                    Toast.makeText(ProfileActivity.this,
                            "修改成功!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"info changed!");
                }
                //修改失败
                else if (listData.get(0).equals(GlobalConstant.FLAG_FAILURE)) {
                    Toast.makeText(ProfileActivity.this,
                            "修改失败!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"info failed to be changed!");
                }
                //未知错误
                else {
                    Toast.makeText(ProfileActivity.this,
                            "ERROR!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"UNKNOWN ERROR!");
                }
            }

            @Override
            public void onDataReceivedFailed() {
            }
        });
    }


    public void back(View view){
        finish();
    }

}
