package com.example.beikeapp.Util.ProfileUtil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.beikeapp.R;

public class ProfileDetailsEditGenderActivity extends AppCompatActivity {

    private RadioGroup rgProfileGender;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_details_edit_gender);

        gender = getIntent().getStringExtra("data");

        initView();
    }

    private void initView() {
        rgProfileGender = findViewById(R.id.rg_profile_gender);
        rbMale = findViewById(R.id.rb_profile_male);
        rbFemale = findViewById(R.id.rb_profile_female);
        if (gender.equals("女")){
            rbFemale.setChecked(true);
        }
    }


    public void save(View view){
        setResult(RESULT_OK, new Intent().putExtra("data",
                rgProfileGender.getCheckedRadioButtonId()==R.id.rb_profile_male?"男":"女"));
        finish();
    }

    public void back(View view) {
        finish();
    }

}
