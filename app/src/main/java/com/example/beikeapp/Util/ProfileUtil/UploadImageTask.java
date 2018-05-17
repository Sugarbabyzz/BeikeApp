package com.example.beikeapp.Util.ProfileUtil;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by m1821 on 2018/5/17.
 */

public class UploadImageTask extends AsyncTask<String,Void,String> {

    private static final String TAG = "UploadImageTaskLog";

    private String path;

    public UploadImageTask(String path) {
        this.path = path;
    }

    @Override
    protected String doInBackground(String... strings) {
        return UploadImage.uploadFile(path);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

            Log.d(TAG,s);

    }
}
