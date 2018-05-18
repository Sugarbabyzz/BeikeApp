package com.example.beikeapp.Util.ProfileUtil;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by m1821 on 2018/5/17.
 */

public class UploadFileTask extends AsyncTask<String,Void,String> {

    private static final String TAG = "UploadImageTaskLog";

    private String path;

    private String id;

    private String account;

    public UploadFileTask(String path, String id,String account) {
        this.path = path;
        this.id = id;
        this.account = account;
    }

    @Override
    protected String doInBackground(String... strings) {
        return UploadFile.upload(path,id,account);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

            Log.d(TAG,s);

    }
}
