package com.example.beikeapp.Util.ProfileUtil;

import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beikeapp.R;

import java.io.File;

public class CleanCacheActivity extends AppCompatActivity {

    private String sdPath;

    private File outCachePath;

    private File outFilePath;

    private TextView tvCacheSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.my_setting_clean_cache);

        tvCacheSize = findViewById(R.id.tv_cache_size);

        initCacheSize();
    }

    /**
     * 获取SD卡根目录：Environment.getExternalStorageDirectory().getAbsolutePath();
     * 外部Cache路径：/mnt/sdcard/android/data/com.xxx.xxx/cache 一般存储缓存数据（注：通过getExternalCacheDir()获取）
     * 外部File路径：/mnt/sdcard/android/data/com.xxx.xxx/files 存储长时间存在的数据
     * (注：通过getExternalFilesDir(String type)获取， type为特定类型，可以是以下任何一种
     * Environment.DIRECTORY_MUSIC,
     * Environment.DIRECTORY_PODCASTS,
     * Environment.DIRECTORY_RINGTONES,
     * Environment.DIRECTORY_ALARMS,
     * Environment.DIRECTORY_NOTIFICATIONS,
     * Environment.DIRECTORY_PICTURES,
     * Environment.DIRECTORY_MOVIES.)
     */
    private void initCacheSize() {

        sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        outCachePath = getApplication().getExternalCacheDir();
        outFilePath = getApplication().getExternalFilesDir(Environment.DIRECTORY_ALARMS);

        try {
            String outCacheSize = CacheManager.getCacheSize(outCachePath);
            String outFileSize = CacheManager.getCacheSize(outFilePath);

            tvCacheSize.setText(outCacheSize);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * do clean cache
     * @param view
     */
    public void clean(View view){
        CacheManager.cleanExternalCache(this);
        Toast.makeText(this,"清理完成",Toast.LENGTH_SHORT).show();
        //重新获取一次缓存大小，自处理M，byte
        initCacheSize();
    }

    public void back(View view){
        finish();
    }
}
