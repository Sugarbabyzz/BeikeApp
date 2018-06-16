package com.example.beikeapp.Util.ProfileUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.beikeapp.Constant.GlobalConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.BaseActivity;
import com.example.beikeapp.Util.MyAsyncTask;
import com.hyphenate.chat.EMClient;

import java.util.List;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SettingActivity";

    private RelativeLayout rlChangePsw;

    private RelativeLayout rlEmptyCache;

    private RelativeLayout rlAboutUs;

    private ProgressDialog progressDialog;

    private static final int REQUEST_CODE_CHANGE_PSW = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.my_setting_main);

        initView();
    }

    private void initView() {
        rlChangePsw = findViewById(R.id.rl_change_password);
        rlEmptyCache = findViewById(R.id.rl_empty_cache);
        rlAboutUs = findViewById(R.id.rl_about_us);
        rlChangePsw.setOnClickListener(this);
        rlEmptyCache.setOnClickListener(this);
        rlAboutUs.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_change_password:
                startActivityForResult(new Intent(this,ChangePswActivity.class),REQUEST_CODE_CHANGE_PSW);
                break;
            case R.id.rl_empty_cache:
                startActivity(new Intent(this,CleanCacheActivity.class));

                break;
            case R.id.rl_about_us:
                startActivity(new Intent(this,AboutUsActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(SettingActivity.this);
                progressDialog.setMessage("Something is happening...");
                progressDialog.setCanceledOnTouchOutside(false);
            }
            if (requestCode == REQUEST_CODE_CHANGE_PSW){
                final String password = data.getStringExtra("data");
                if (!TextUtils.isEmpty(password)) {
                    progressDialog.setMessage("正在修改密码...");
                    progressDialog.show();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ChangePswTask(SettingActivity.this,password);

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
                                    Toast.makeText(SettingActivity.this, "fail to change password!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        }
    }

    /**
     * 更新数据库信息
     *
     * @param context
     */
    private void ChangePswTask(Context context, String data) {

        String url = GlobalConstant.URL_CHANGE_INFO
                + "?id=" + BaseId
                + "&account=" + EMClient.getInstance().getCurrentUser()
                + "&columnName=" + "Password"
                + "&data=" + data;
        MyAsyncTask a = new MyAsyncTask(context);
        a.execute(url);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {
                //修改成功
                if (listData.get(0).equals(GlobalConstant.FLAG_SUCCESS)) {

                    Toast.makeText(SettingActivity.this,
                            "修改成功!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "info changed!");
                }
                //修改失败
                else if (listData.get(0).equals(GlobalConstant.FLAG_FAILURE)) {
                    Toast.makeText(SettingActivity.this,
                            "修改失败!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "info failed to be changed!");
                }
                //未知错误
                else {
                    Toast.makeText(SettingActivity.this,
                            "ERROR!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "UNKNOWN ERROR!");
                }
            }

            @Override
            public void onDataReceivedFailed() {
            }
        });
    }

    public void back(View view) {
        finish();
    }
}
