package com.example.beikeapp.Util.ProfileUtil;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.beikeapp.R;
import com.example.beikeapp.Util.BaseActivity;
import com.example.beikeapp.Util.ProfileUtil.clipimage.ClipImageActivity;

import java.io.File;
import java.io.IOException;

public class ProfileEditPhotoActivity extends BaseActivity {

    private final String TAG = "ProfileEditPhotoErr";


    private final int REQUEST_CODE_OPEN_GALLERY = 1;

    private final int REQUEST_CODE_OPEN_CAMERA = 2;

    private final int getREQUEST_CODE_CUT_IMAGE = 3;

    private boolean FLAG_CLIPED;

    private ImageView ivPhoto;

    private ImageView ivPhotoPreview;

    private String imagePath = "";

    private String clippedPath = "";

    private String mOutputPath;

    private Bitmap bitmap;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_edit_photo);
        //图片是否经过裁剪的标志位
        FLAG_CLIPED = false;

        bitmap = ProfileInfo.isSet ? ProfileInfo.bitmap : null;

        ivPhoto = findViewById(R.id.iv_profile_photo);
        ivPhotoPreview = findViewById(R.id.iv_profile_photo_preview);
        ivPhoto.setImageBitmap(bitmap);
        ivPhotoPreview.setImageBitmap(bitmap);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("                               设置头像");
        }
    }

    /**
     * 从相册中获取图片
     */
    public void select_photo() {
        openAlbum();
    }

    /**
     * 打开相册的方法
     */
    private void openAlbum() {
        //Intent i = new Intent(Intent.ACTION_PICK);
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_OPEN_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_OPEN_GALLERY:
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT > 19) {
                        // > 4.4
                        handleImageOnKitKat(data);
                    } else {
                        // <= 4.4
                        handleImageBeforeKitKat(data);
                    }
                    break;

                case REQUEST_CODE_OPEN_CAMERA:
                    imagePath = file.getAbsolutePath();
                    Bitmap bm = BitmapFactory.decodeFile(imagePath);
                    ivPhoto.setImageBitmap(bm);
                    ivPhotoPreview.setImageBitmap(bm);

                    //在手机相册中显示刚拍摄的图片
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(file);
                    mediaScanIntent.setData(contentUri);
                    sendBroadcast(mediaScanIntent);
                    break;
                case getREQUEST_CODE_CUT_IMAGE:
                    clippedPath = ClipImageActivity.ClipOptions.createFromBundle(data).getOutputPath();
                    if (clippedPath != null) {
                        FLAG_CLIPED = true;
                        Bitmap bitmap = BitmapFactory.decodeFile(clippedPath);
                        ivPhoto.setImageBitmap(bitmap);
                        ivPhotoPreview.setImageBitmap(bitmap);
                        break;
                    }
            }
        }
    }

    /**
     * 系统4.4之前,获取uri很简单
     *
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    /**
     * 4.4及以上系统处理图片的方法
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //解析出数字格式的id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                //如果是content类型的uri，则使用普通方式处理
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                //如果是file类型的uri，直接获取图片路径即可
                imagePath = uri.getPath();

            }
            //根据图片路径显示图片
            displayImage(imagePath);
        }
    }

    /**
     * 本地方法,根据本地路径显示图片
     * 根据图片路径显示图片的方法
     */
    private void displayImage(String imagePath) {
        FLAG_CLIPED = false;

        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            ivPhoto.setImageBitmap(bitmap);
            ivPhotoPreview.setImageBitmap(bitmap);
            this.imagePath = imagePath;

        } else {
            Toast.makeText(this, "fail to display image!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 通过uri和selection来获取真实的图片路径
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 在菜单栏构建按钮
     * 使用Menu包下的menu布局文件
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_profile_edit_photo_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * menu下的按钮的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_use_gallery://from gallery
                select_photo();
                return true;
            case R.id.menu_use_camera: //from camera
                try {
                    askForPermission();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.menu_use_clip:
                if (imagePath.isEmpty()) {
                    Toast.makeText(this, "请选择图片后再裁剪!", Toast.LENGTH_SHORT).show();
                } else {
                    mOutputPath = new File(getExternalCacheDir(), "output.jpg").getPath();

                    ClipImageActivity.prepare()
                            .aspectX(3).aspectY(2)
                            .inputPath(imagePath).outputPath(mOutputPath)
                            .startForResult(this, getREQUEST_CODE_CUT_IMAGE);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * open cam
     */
    private void openCam() throws IOException {


            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                     + System.currentTimeMillis() + ".jpg");
            file.createNewFile();
            Uri uri;
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(this, "com.example.beikeapp.fileprovider", file);
            }else {
                uri = Uri.fromFile(file);
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //添加权限
            //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, REQUEST_CODE_OPEN_CAMERA);

    }

    public void askForPermission() throws IOException {

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= 23) {
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DENIED---拒绝
            if (check == PackageManager.PERMISSION_GRANTED) {
                //调用相机
                openCam();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);
            }
        } else {
            openCam();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 4 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                openCam();
            } catch (IOException e) {
                Log.d(TAG,e.getMessage());
            }
        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * upload photo to server
     * do in ProfileActivity
     *
     * @param view
     */
    public void upload(View view) {
        if (imagePath.isEmpty()) {
            Toast.makeText(this, "这已是您的头像!", Toast.LENGTH_SHORT).show();
            return;
        }
        setResult(RESULT_OK, new Intent().putExtra("data", FLAG_CLIPED ? clippedPath : imagePath));
        finish();
    }
}
