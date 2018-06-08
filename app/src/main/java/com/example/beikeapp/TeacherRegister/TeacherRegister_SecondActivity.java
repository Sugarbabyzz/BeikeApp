package com.example.beikeapp.TeacherRegister;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.andreabaccega.widget.FormEditText;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.beikeapp.Constant.TeacherConstant;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.AsyncResponse;
import com.example.beikeapp.Util.MyAsyncTask;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import mabbas007.tagsedittext.TagsEditText;

public class TeacherRegister_SecondActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TeacherRegister_Second";
    //上一活动传来的手机和密码
    private String account, password;
    //带错误校验的输入框
    private FormEditText etName;
    private FormEditText etSchool;

    private RadioGroup rgGender;

    private Button registerBtn;
    private Button addClassBtn;
    private Button btnRemoveClass;
    //滑动选择器，用于选择班级
    private OptionsPickerView pvNoLinkOptions;
    //添加的班级列表
    private TagsEditText mTagsEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_register_second);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        account = intent.getStringExtra("account");
        password = intent.getStringExtra("password");
        etName = findViewById(R.id.et_name);
        etSchool = findViewById(R.id.et_school);
        rgGender = findViewById(R.id.rg_teacher_gender);
        registerBtn = findViewById(R.id.button_register);
        addClassBtn = findViewById(R.id.button_addClass);
        btnRemoveClass = findViewById(R.id.button_remove_class);
        addClassBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        btnRemoveClass.setOnClickListener(this);
        mTagsEdit = findViewById(R.id.tagsEditText);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_register: //注册
                String name = etName.getText().toString();
                String school = etSchool.getText().toString();
                String gender = rgGender.getCheckedRadioButtonId() == R.id.rb_male ? "男" : "女";
                //从tagsList获取List并转换成String类型，用","隔开
                List<String> tagsList = mTagsEdit.getTags();
                String classes = StringUtils.join(tagsList, ",");

                //注册至环信后台
                registerToHX(account, password);
                //注册至我们自己的服务器
                registerToUs(name, gender, school, classes);

                break;
            case R.id.button_addClass: //添加班级
                createPicker();
                break;
            case R.id.button_remove_class: //删除上一个添加的班级
                removeClass();
            default:
                break;
        }
    }

    /**
     * 删除已选择的上一个班级
     */
    private void removeClass() {
        List<String> tagsList = mTagsEdit.getTags();
        // 判断tagsList是否已经为空
        if (tagsList.isEmpty()) {
            Log.d(TAG, "removeClass: tagsList empty!");
            return;
        }
        // 非空，remove掉上一个班级名
        tagsList.remove(tagsList.size() - 1);
        String[] tagsArray = new String[tagsList.size()];
        tagsList.toArray(tagsArray);
        mTagsEdit.setTags(tagsArray);
    }

    /**
     * 创建滑动选择器
     */
    private void createPicker() {
        initNoLinkOptionsPicker();
        pvNoLinkOptions.show();
    }

    /**
     * 初始化滑动选择器的数值
     */
    private void initNoLinkOptionsPicker() {// 不联动的多级选项
        final ArrayList<String> grade = new ArrayList<>();
        final ArrayList<String> constant = new ArrayList<>();
        final ArrayList<String> classes = new ArrayList<>();

        grade.add("一");
        grade.add("二");
        grade.add("三");
        grade.add("四");
        grade.add("五");
        grade.add("六");
        constant.add("年级");
        classes.add("(1)班");
        classes.add("(2)班");
        classes.add("(3)班");
        classes.add("(4)班");
        classes.add("(5)班");
        classes.add("(6)班");
        classes.add("(7)班");
        classes.add("(8)班");
        classes.add("(9)班");
        classes.add("(10)班");
        classes.add("(11)班");
        classes.add("(12)班");

        pvNoLinkOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                List<String> tagsList = mTagsEdit.getTags();
                String[] tagsArray = new String[tagsList.size() + 1];
                tagsList.toArray(tagsArray);

                String str = grade.get(options1)
                        + "年级"
                        + classes.get(options3);
                tagsArray[tagsArray.length - 1] = str;

                mTagsEdit.setTags(tagsArray);
            }


        }).build();
        pvNoLinkOptions.setNPicker(grade, constant, classes);
    }


    /**
     * 注册至环信服务器
     *
     * @param account
     * @param password
     */
    public void registerToHX(final String account, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(account, password);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 注册至我们的服务器
     *
     * @param name
     * @param gender
     * @param school
     * @param classes
     */
    public void registerToUs(String name, String gender, String school, String classes) {
        String urlString = TeacherConstant.URL_BASIC + TeacherConstant.URL_REGISTER
                + "?account=" + account
                + "&password=" + password
                + "&name=" + name
                + "&gender=" + gender
                + "&school=" + school
                + "&classes=" + classes;
        MyAsyncTask a = new MyAsyncTask(this);
        a.execute(urlString);
        a.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(List<String> listData) {
                startActivity(new Intent(TeacherRegister_SecondActivity.this,
                        TeacherRegister_ThirdActivity.class).putExtra("response", listData.get(0)));
            }

            @Override
            public void onDataReceivedFailed() {
            }
        });

    }

}

