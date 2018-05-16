package com.example.beikeapp.Util.ProfileUtil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.beikeapp.R;
import com.example.beikeapp.Util.ChatUtil.ChatBaseActivity;

import java.util.ArrayList;
import java.util.List;

import mabbas007.tagsedittext.TagsEditText;

public class ProfileDetailsEditClassActivity extends ChatBaseActivity {

    private TagsEditText mTagsEdit;

    private Button btnAddClass;

    private OptionsPickerView pvNoLinkOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_details_edit_class);

        initView();
        //若class只有一个，该数组则就只有一个元素
        String[] classArray = getIntent().getStringExtra("data").split(",");
        mTagsEdit.setTags(classArray);
    }

    private void initView() {
        mTagsEdit = findViewById(R.id.tagsEditText_profile_class);
        btnAddClass = findViewById(R.id.button_addClass);
        btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPicker(); //构建一个滑动选择器
            }
        });
    }

    /**
     * 新建滑动选择器
     */
    private void createPicker() {
        initNoLinkOptionsPicker();
        pvNoLinkOptions.show();
    }

    /**
     * 设置滑动选择器数据源
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


    public void save(View view){
        setResult(RESULT_OK, new Intent().putExtra("data",(ArrayList)mTagsEdit.getTags()));
        finish();
    }

    public void back(View view) {
        finish();
    }

}
