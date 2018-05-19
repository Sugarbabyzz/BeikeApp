package com.example.beikeapp.Util.ProfileUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.beikeapp.Constant.GlobalConstant;

/**
 * Created by m1821 on 2018/5/17.
 */

public class UploadFileTask extends AsyncTask<String,Void,String> {

    private static final String TAG = "UploadImageTaskLog";

    private String path;

    private String id;

    private String account;

    private Context context;

    public UploadFileTask(String path, String id,String account,Context context) {
        this.path = path;
        this.id = id;
        this.account = account;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        return UploadFile.upload(path,id,account);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s.equals(GlobalConstant.FLAG_SUCCESS)){
            Toast.makeText(context,"上传成功!",Toast.LENGTH_SHORT).show();
        }
        else if (s.equals(GlobalConstant.FLAG_FAILURE)){
            Toast.makeText(context,"上传失败!",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"未知错误!",Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG,s);
    }
}
